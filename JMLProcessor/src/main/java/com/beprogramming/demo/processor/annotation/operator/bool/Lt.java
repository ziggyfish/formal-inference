package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Lt extends BooleanAbstract {

    public Lt(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " < ";
    }

    @Override
    public Object clone() {
        return new Lt(cloneParameters(getParameters()));
    }
}
