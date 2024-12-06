package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.logic.All;

import java.util.Arrays;

public class Arr extends VariableAbstract {
    private OperatorAbstract[] parameters;
    public Arr(OperatorAbstract[] parameters) {
        super(Arrays.toString(parameters));
        this.parameters = parameters;

    }

    @Override
    public Object clone() {
        return new Arr(cloneParameters(getParameters()));
    }
}
