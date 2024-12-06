package com.beprogramming.demo.processor;

import com.beprogramming.demo.processor.annotation.AnnotationFactory;
import com.beprogramming.demo.processor.annotation.operator.Empty;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.control.KeYEnvironment;
import de.uka.ilkd.key.java.abstraction.KeYJavaType;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.label.OriginTermLabel;
import de.uka.ilkd.key.logic.label.TermLabel;
import de.uka.ilkd.key.logic.op.IObserverFunction;
import de.uka.ilkd.key.logic.op.LocationVariable;
import de.uka.ilkd.key.logic.op.ProgramMethod;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.Proof;
import de.uka.ilkd.key.proof.init.ProofInputException;
import de.uka.ilkd.key.proof.io.ProblemLoaderException;
import de.uka.ilkd.key.settings.ChoiceSettings;
import de.uka.ilkd.key.settings.ProofSettings;
import de.uka.ilkd.key.speclang.Contract;
import de.uka.ilkd.key.speclang.FunctionalOperationContract;
import de.uka.ilkd.key.strategy.StrategyProperties;
import de.uka.ilkd.key.util.KeYTypeUtil;
import de.uka.ilkd.key.util.MiscTools;
import org.key_project.util.collection.ImmutableSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.*;

public class SpecificationProcessor {

    public void verifyFile(String program) {
        File location = new File(program);
        try {

            KeYEnvironment<?> env = setupEnvironment(location);

            final List<Contract> proofContracts = getContracts(env);

            for (Contract contract : proofContracts) {
                proveContract(contract, env);
            }

        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private KeYEnvironment<?> setupEnvironment(File location) throws ProblemLoaderException {
        List<File> classPaths = List.of(new File(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getFile()));
        File bootClassPath = null; // Optionally: Different default specifications for Java API
        List<File> includes = null; // Optionally: Additional includes to consider

        if (!ProofSettings.isChoiceSettingInitialised()) {
            KeYEnvironment<?> env =
                    KeYEnvironment.load(location, classPaths, bootClassPath, includes);
            env.dispose();
        }
        // Set Taclet options
        ChoiceSettings choiceSettings = ProofSettings.DEFAULT_SETTINGS.getChoiceSettings();
        Map<String, String> oldSettings = choiceSettings.getDefaultChoices();
        Map<String, String> newSettings = new HashMap<>(oldSettings);
        newSettings.putAll(MiscTools.getDefaultTacletOptions());
        choiceSettings.setDefaultChoices(newSettings);
        // Load source code
        KeYEnvironment<?> env =
                KeYEnvironment.load(location, classPaths, bootClassPath, includes);
        // env.getLoadedProof() returns performed proof if a *.proof file is loaded
        return env;
    }

    public Map<String, Map<String, OperatorAbstract>> getContractAsAnnotations(String program) throws ProblemLoaderException {
        File location = new File(program);

        KeYEnvironment<?> env = setupEnvironment(location);
        return getContractAsAnnotations(env);
    }

    public Map<String, Map<String, OperatorAbstract>> getContractAsAnnotations(KeYEnvironment<?> env){
        final List<Contract> proofContracts = new LinkedList<>();
        Set<KeYJavaType> kjts = env.getJavaInfo().getAllKeYJavaTypes();
        Map<String, Map<String,OperatorAbstract>> annotaions = new HashMap<>();
        for (KeYJavaType type : kjts) {
            if (!KeYTypeUtil.isLibraryClass(type)) {
                ImmutableSet<IObserverFunction> targets =
                        env.getSpecificationRepository().getContractTargets(type);
                for (IObserverFunction target : targets) {

                    ImmutableSet<Contract> contracts =
                            env.getSpecificationRepository().getContracts(type, target);

                    for (Contract contract : contracts) {

                        LocationVariable heap = env.getServices().getTypeConverter().getHeapLDT().getHeap();
                        Map<String, OperatorAbstract> contractMap = new HashMap<>();


                        if (contract instanceof FunctionalOperationContract functionContract) {

                            ProgramMethod method = ((ProgramMethod) contract.getTarget());

                            contractMap.put("rssignable", getAnnotation(functionContract.getAssignable(heap)));
                            contractMap.put("requires", getAnnotation(functionContract.getRequires(heap)));
                            contractMap.put("accessible", getAnnotation(functionContract.getAccessible(heap)));

                            contractMap.put("ensures", getAnnotation(functionContract.getEnsures(heap)));


                            annotaions.put(getFunctionKey(contract.getTarget()), contractMap);
                        }


                    }
                }
            }
        }

        return annotaions;
    }

    private String getFunctionKey(IObserverFunction target) {
        if (target instanceof ProgramMethod programMethod)
            return programMethod.toString();
        return target.toString();
    }

    public List<Contract> getContracts(KeYEnvironment<?> env) {
        final List<Contract> proofContracts = new LinkedList<>();
        Set<KeYJavaType> kjts = env.getJavaInfo().getAllKeYJavaTypes();
        for (KeYJavaType type : kjts) {
            if (!KeYTypeUtil.isLibraryClass(type)) {
                ImmutableSet<IObserverFunction> targets =
                        env.getSpecificationRepository().getContractTargets(type);
                for (IObserverFunction target : targets) {

                    ImmutableSet<Contract> contracts =
                            env.getSpecificationRepository().getContracts(type, target);

                    for (Contract contract : contracts) {

                        proofContracts.add(contract);

                    }
                }
            }
        }
        return proofContracts;
    }

    public boolean isLabelFromFile(Term term) {
        for (TermLabel label : term.getLabels()) {
            if (label instanceof OriginTermLabel && ((OriginTermLabel) label).getOrigin() instanceof OriginTermLabel.FileOrigin) {
                return true;
            }
        }
        return false;

    }

    public Term findOrigin(Term term) {

        if (term == null)
            return null;

        if (isLabelFromFile(term))
            return term;

        for (Term sub : term.subs()) {
            Term origin = findOrigin(sub);
            if (origin != null)
                return origin;
        }
        return null;
    }

    public OperatorAbstract getAnnotation(Term term) {
        term = findOrigin(term);


        if (term == null)
            return null;
        try {
            OperatorAbstract annotationLine = AnnotationFactory.getMapper(term).toAnnotation(term);
            if (annotationLine instanceof Empty)
                return null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(annotationLine);
            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println(annotationLine.getClass().getCanonicalName());

            return annotationLine;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void proveContract(Contract contract, KeYEnvironment<?> env) throws ProofInputException {
        Proof proof;
        proof = env.createProof(contract.createProofObl(env.getInitConfig(), contract));


        // Set proof strategy options
        StrategyProperties sp = proof.getSettings().getStrategySettings().getActiveStrategyProperties();

        sp.setProperty(StrategyProperties.METHOD_OPTIONS_KEY,
                StrategyProperties.METHOD_CONTRACT);
        sp.setProperty(StrategyProperties.DEP_OPTIONS_KEY,
                StrategyProperties.DEP_ON);
        sp.setProperty(StrategyProperties.QUERY_OPTIONS_KEY,
                StrategyProperties.QUERY_ON);
        sp.setProperty(StrategyProperties.LOOP_OPTIONS_KEY,
                StrategyProperties.LOOP_SCOPE_INV_TACLET);
        sp.setProperty(StrategyProperties.NON_LIN_ARITH_OPTIONS_KEY,
                StrategyProperties.NON_LIN_ARITH_DEF_OPS);
        sp.setProperty(StrategyProperties.STOPMODE_OPTIONS_KEY,
                StrategyProperties.STOPMODE_NONCLOSE);

        proof.getSettings().getStrategySettings().setActiveStrategyProperties(sp);
        System.out.println("===================== Proving =======================================");
        env.getUi().getProofControl().startAndWaitForAutoMode(proof);
        boolean closed = proof.openGoals().isEmpty();
        System.out.print("Contract '");
        System.out.print(contract.getDisplayName());
        System.out.print("' of ");
        System.out.print(contract.getTarget());
        System.out.print(" is ");
        System.out.println(closed ? "verified" : "still open");
        for (Goal g : proof.openGoals()) {
            System.out.println(g.sequent());
        }
    }
}
