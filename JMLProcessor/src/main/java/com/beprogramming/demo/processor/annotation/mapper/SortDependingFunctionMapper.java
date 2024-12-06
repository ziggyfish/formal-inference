package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.func.Select;
import com.beprogramming.demo.processor.annotation.operator.func.TypedFunction;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.SortDependingFunction;

public class SortDependingFunctionMapper extends AbstractMapper<SortDependingFunction> {
    public SortDependingFunctionMapper(SortDependingFunction operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {


        if (operator.getKind().toString().equals("select")) {
            return new Select(operator.getKind().toString(), operator.getSortDependingOn().toString(), processSubArguments(term));
        }


        return new TypedFunction(operator.getKind().toString(), operator.getSortDependingOn().toString(), processSubArguments(term));
    }
}
