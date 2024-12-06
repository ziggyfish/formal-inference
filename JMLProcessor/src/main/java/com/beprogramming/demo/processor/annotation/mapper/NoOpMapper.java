package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Operator;

public class NoOpMapper extends AbstractMapper {

    public NoOpMapper(Operator operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        return null;
    }
}
