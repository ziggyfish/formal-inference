package com.beprogramming.demo.processor;


import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.io.File;
import java.util.*;

import com.beprogramming.demo.processor.analysis.ControlFlowGraph;
import com.beprogramming.demo.processor.annotation.operator.Empty;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.bool.And;
import com.beprogramming.demo.processor.annotation.operator.bool.Eq;
import com.beprogramming.demo.processor.annotation.operator.bool.Or;
import com.beprogramming.demo.processor.annotation.operator.bool.True;
import com.beprogramming.demo.processor.annotation.operator.func.Imp;
import com.beprogramming.demo.processor.annotation.operator.var.Variable;
import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.springframework.javapoet.*;

@javax.annotation.processing.SupportedAnnotationTypes("com.beprogramming.demo.processor.ValidateAnnotation")
@javax.annotation.processing.SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_17)
public class JMLAnnotationProcessor extends AbstractProcessor {
    private Trees trees;
    private SpecificationProcessor specificationProcessor;
    private Filer filer;
    private Messager messager;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // Initialization code, if needed
        trees = Trees.instance(processingEnv);
        specificationProcessor = new SpecificationProcessor();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return  SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ValidateAnnotation.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {

                for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

                    try {
                        CompilationUnitTree cu = trees.getPath(element).getCompilationUnit();
                        String typeName = getFileNameWithoutExtension(cu.getSourceFile().getName());

                        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(typeName.concat("Spec"));


                        Map<String, Map<String, OperatorAbstract>> specifications = specificationProcessor.getContractAsAnnotations(trees.getPath(element).getCompilationUnit().getSourceFile().toUri().getPath());

                        TreePathScannerToJavaPoet scanner = new TreePathScannerToJavaPoet(typeSpec, trees, trees.getPath(element).getCompilationUnit(), specifications);

                        scanner.scan(trees.getPath(element).getCompilationUnit(), null);


                        String fullyQualifiedName = Utils.concat(cu.getPackageName().toString(), ".", typeName);
                        for (FieldSpec fieldSpec : scanner.fieldSpecs) {
                            typeSpec.addField(fieldSpec);
                        }

                        for (Tuple<String, MethodSpec.Builder> builderTuple : scanner.methodBuilders) {

                            String methodName = Utils.concat(fullyQualifiedName, "::", builderTuple.x);

                            if (specifications.containsKey(methodName)) {
                                Map<String, OperatorAbstract> spec = specifications.get(methodName);
                                MethodSpec.Builder methodBuilder = builderTuple.y;
                                AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(Specification.class);
                                for (Map.Entry<String, OperatorAbstract> entry : spec.entrySet()) {

                                    if (entry.getValue() != null) {
                                        System.out.println(entry.getValue().toString());
                                        for (OperatorAbstract operator : entry.getValue().getParameters()) {
                                            System.out.println(operator.getClass().getCanonicalName());
                                        }

                                        annotationSpec.addMember(entry.getKey(), Utils.wrapString(entry.getValue().toString()));
                                    }
                                }

                                methodBuilder.addAnnotation(annotationSpec.build());
                                typeSpec.addMethod(methodBuilder.build());
                            } else {
                                typeSpec.addMethod(builderTuple.y.build());
                            }
                        }

                        JavaFile file = JavaFile.builder(cu.getPackageName().toString(), typeSpec.build()).build();

                        file.writeTo(filer);
                        
                        //specificationProcessor.verifyFile(trees.getPath(element).getCompilationUnit().getSourceFile().toUri().getPath());


                    }catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

        return true;
    }

    private static String getFileNameWithoutExtension(String path){
        String fileName = path.substring( path.lastIndexOf(File.separatorChar)+1, path.length() );

        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static class TreePathScannerToJavaPoet extends TreePathScanner<Void, Void> {
        private TypeSpec.Builder typeSpecBuilder;
        private Trees trees;
        private CompilationUnitTree cu;
        private List<Tuple<String, MethodSpec.Builder>> methodBuilders = new ArrayList<>();
        private List<FieldSpec> fieldSpecs = new ArrayList<>();
        private Map<String, Map<String, OperatorAbstract>> specifications;
        private Stack<String> methodStack = new Stack<>();
        private ControlFlowGraph methodControlFlowGraph;
        private Set<String> ignoredMethods = new HashSet<>();
        public final Map<String, ControlFlowGraph.ProgramVariable> variables = new HashMap<>();

        public TreePathScannerToJavaPoet(TypeSpec.Builder typeSpecBuilder, Trees trees, CompilationUnitTree compilationUnit, Map<String, Map<String, OperatorAbstract>> specifications) {
            this.typeSpecBuilder = typeSpecBuilder;
            this.trees = trees;
            this.cu = compilationUnit;
            this.specifications = specifications;
            for(Map.Entry<String, Map<String, OperatorAbstract>> entry : specifications.entrySet()) {
                ignoredMethods.add(entry.getKey());
            }
        }


        @Override
        public Void visitVariable(VariableTree node, Void unused) {

            VariableElement el = (VariableElement) trees.getElement(trees.getPath(cu, node));
            if (el.getKind() == ElementKind.FIELD) {

                FieldSpec.Builder spec = FieldSpec.builder(TypeName.get(el.asType()), node.getName().toString(), node.getModifiers().getFlags().toArray(new Modifier[0]));

                ControlFlowGraph.ProgramVariable var = new ControlFlowGraph.ProgramVariable(node, el.getKind(), new True(new OperatorAbstract[]{}));

                variables.put(var.name, var);
                spec.initializer(node.getInitializer().toString());
                fieldSpecs.add(spec.build());

            }
            if (methodControlFlowGraph != null) {
                methodControlFlowGraph.processVariable(node);
            }

            return null;
        }

        @Override
        public Void visitMethod(MethodTree node, Void unused) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(node.getName().toString());
            if (node.getReturnType() != null) {
                methodBuilder = copyMethod((ExecutableElement) trees.getElement(trees.getPath(cu, node)));
            }

            String body = node.getBody().toString().trim();
            for (StatementTree tree : node.getBody().getStatements()){
                System.out.println(tree.getKind());
            }

            if (body.startsWith("{")) {
                body = body.substring(1);
            }
            if (body.endsWith("}")) {
                body = body.substring(0, body.length() - 1);
            }


            methodBuilder.addCode(body);

            methodBuilders.add(new Tuple<>(node.getName().toString(), methodBuilder));
            ExecutableElement element = (ExecutableElement)trees.getElement(trees.getPath(cu, node));

            if (ignoredMethods.contains(Utils.getMethodName(element)))
                return null;
            methodControlFlowGraph = new ControlFlowGraph(node, trees, cu, specifications, variables);
            methodStack.add(Utils.getMethodName(element));

            Void retVal = super.visitMethod(node, unused);

            methodControlFlowGraph.processAssignments();

            methodControlFlowGraph = null;
            methodStack.pop();

            return retVal;
        }

        @Override
        public Void visitAssignment(AssignmentTree node, Void unused) {
            methodControlFlowGraph.processAssignment(node);
            return super.visitAssignment(node, unused);
        }

        @Override
        public Void visitIf(IfTree node, Void unused) {
            methodControlFlowGraph.processIf(node, this);
            return null;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {

            ExecutableElement element = (ExecutableElement)trees.getElement(trees.getPath(cu, node));
            String methodName = Utils.getMethodName(element);
            Map<String, OperatorAbstract> methodSpec = specifications.get(methodName);

            if (methodSpec == null)
                return null;

            String currentMethod = methodStack.peek();

            if (!specifications.containsKey(currentMethod)) {
                specifications.put(currentMethod, new HashMap<>());
            }

            List<? extends ExpressionTree> arguments = node.getArguments();
            List<? extends VariableElement> parameters = element.getParameters();

            Map<String, ControlFlowGraph.ProgramVariable> substitutedElements = new HashMap<>();
            for (int i = 0; i < arguments.size(); i++) {

                String parameterName = parameters.get(i).getSimpleName().toString();
                OperatorAbstract expression = methodControlFlowGraph.substituteVariable(methodControlFlowGraph.expressionToOperatorAbstract(arguments.get(i)), methodControlFlowGraph.variables);
                ControlFlowGraph.ProgramVariable newVar = new ControlFlowGraph.ProgramVariable(parameterName, parameters.get(i).getKind(),expression);
                substitutedElements.put(parameterName, newVar);

            }

            Map<String, OperatorAbstract> currentMethodSpec = specifications.get(currentMethod);
            Map<String, OperatorAbstract> callingMethodSpec = specifications.get(methodName);
            if (currentMethodSpec == null) {
                currentMethodSpec = new HashMap<>();
            }

            if (callingMethodSpec == null) {
                return null;
            }

            OperatorAbstract requires = callingMethodSpec.get("requires");

            if (!methodControlFlowGraph.conditions.isEmpty()) {
                requires = new Imp(new OperatorAbstract[]{ methodControlFlowGraph.getCurrentConditions(), requires});
            }

            if (requires != null) {


                OperatorAbstract newEnsures = methodControlFlowGraph.substituteVariable((OperatorAbstract) requires.clone(), substitutedElements);

                OperatorAbstract oldRequires = currentMethodSpec.get("requires");
                if (oldRequires == null) {
                    currentMethodSpec.put("requires", newEnsures);
                } else {
                    currentMethodSpec.put("requires", new And(new OperatorAbstract[]{currentMethodSpec.get("requires"), (OperatorAbstract) newEnsures.clone()}));
                }
            }

            if (!specifications.containsKey(currentMethod))
                specifications.put(currentMethod, new HashMap<>());

            specifications.get(currentMethod).put("requires", currentMethodSpec.get("requires"));

            return null;
            //return super.visitMethodInvocation(node, unused);
        }



        @Override
        public Void visitForLoop(ForLoopTree node, Void unused) {

            return super.visitForLoop(node, unused);
        }

        @Override
        public Void visitReturn(ReturnTree node, Void unused) {
            String currentMethod = methodStack.peek();

            if (!specifications.containsKey(currentMethod)) {
                specifications.put(currentMethod, new HashMap<>());
            }

            Map<String, OperatorAbstract> methodSpec = specifications.get(currentMethod);
            OperatorAbstract ensures = methodSpec.get("ensures");

            OperatorAbstract expression = methodControlFlowGraph.expressionToOperatorAbstract(node.getExpression());
            OperatorAbstract requires = methodControlFlowGraph.getOptimizedAnd(expression);
            if (!requires.toString().contains("result")) {
                requires = new Eq(new OperatorAbstract[]{new Variable("\\\\result"), expression});
            }

            methodSpec.put("ensures", methodControlFlowGraph.getOptimizedOr(ensures, requires));
            return null;
        }

        private MethodSpec.Builder copyMethod(ExecutableElement method) {

            Set<Modifier> modifiers = method.getModifiers();

            String methodName = method.getSimpleName().toString();
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName);
            Set<Modifier> newModifiers = new LinkedHashSet<>(modifiers);
            newModifiers.remove(Modifier.ABSTRACT);
            newModifiers.remove(Modifier.DEFAULT);
            methodBuilder.addModifiers(newModifiers);
            Iterator var5 = method.getTypeParameters().iterator();

            while (var5.hasNext()) {
                TypeParameterElement typeParameterElement = (TypeParameterElement) var5.next();
                TypeVariable var = (TypeVariable) typeParameterElement.asType();
                methodBuilder.addTypeVariable(TypeVariableName.get(var));
            }

            methodBuilder.returns(TypeName.get(method.getReturnType()));
            methodBuilder.addParameters(parametersOf(method));
            methodBuilder.varargs(method.isVarArgs());
            var5 = method.getThrownTypes().iterator();

            while (var5.hasNext()) {
                TypeMirror thrownType = (TypeMirror) var5.next();
                methodBuilder.addException(TypeName.get(thrownType));
            }

            return methodBuilder;


        }
        static List<ParameterSpec> parametersOf(ExecutableElement method) {
            List<ParameterSpec> result = new ArrayList();
            Iterator var2 = method.getParameters().iterator();

            while(var2.hasNext()) {
                VariableElement parameter = (VariableElement)var2.next();
                result.add(get(parameter));
            }

            return result;
        }
        public static ParameterSpec get(VariableElement element) {
            TypeName type = TypeName.get(element.asType());
            String name = element.getSimpleName().toString();
            return ParameterSpec.builder(type, name).addModifiers(element.getModifiers()).build();
        }
    }
}

class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}