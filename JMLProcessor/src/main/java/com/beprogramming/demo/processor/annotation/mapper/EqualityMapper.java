package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.bool.Eq;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Equality;

public class EqualityMapper extends AbstractMapper<Equality> {
    public EqualityMapper(Equality operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {
        return new Eq(processSubArguments(term));
    }
}
