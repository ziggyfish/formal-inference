package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.var.Variable;
import de.uka.ilkd.key.logic.ProgramElementName;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.LocationVariable;

import java.util.Objects;

public class LocationVariableMapper extends AbstractMapper<LocationVariable> {
    public LocationVariableMapper(LocationVariable operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) {
        if (Objects.equals(operator.name().toString(), "result_add")) {
            return new Variable("\\\\result");
        }
        return new Variable(getName(operator.name()).toString());
    }
}
