package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.func.Function;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.ObserverFunction;

public class ObserverFunctionMapper extends AbstractMapper<ObserverFunction> {
    public ObserverFunctionMapper(ObserverFunction operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        return new Function(operator.name().toString(), processSubArguments(term));
    }
}
