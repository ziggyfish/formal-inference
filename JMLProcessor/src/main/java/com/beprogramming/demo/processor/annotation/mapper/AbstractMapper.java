package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.AnnotationFactory;
import com.beprogramming.demo.processor.annotation.operator.*;
import com.beprogramming.demo.processor.annotation.operator.var.GlobalVariable;
import com.beprogramming.demo.processor.annotation.operator.var.LocalVar;
import com.beprogramming.demo.processor.annotation.operator.var.Value;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.ProgramElementName;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Operator;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractMapper<T extends Operator> {
    protected T operator;
    public AbstractMapper(T operator) {
        this.operator = operator;
    }

    abstract public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException;

    protected OperatorAbstract getName(Name name) {

        if (name instanceof ProgramElementName) {
            ProgramElementName elementName = ((ProgramElementName) operator.name());

            if (elementName.getQualifier().isEmpty())
                return new LocalVar(elementName.getProgramName());

            return new GlobalVariable(elementName.getQualifier(), elementName.getProgramName());
        }

        return new Value(name.toString());
    }


    protected OperatorAbstract[] processSubArguments(Term term) throws ClassNotFoundException {
        List<OperatorAbstract> args = new ArrayList<>();

        for(Term param: term.subs()) {
            OperatorAbstract operator = AnnotationFactory.getMapper(param).toAnnotation(param);
            if (operator != null)
                args.add(AnnotationFactory.getMapper(param).toAnnotation(param));
        }

        return args.toArray(new OperatorAbstract[0]);
    }
}
