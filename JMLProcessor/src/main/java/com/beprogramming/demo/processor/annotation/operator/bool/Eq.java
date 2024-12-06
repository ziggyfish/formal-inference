package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Eq extends BooleanAbstract {

    public Eq(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " == ";
    }

    @Override
    public Object clone() {
        return new Eq(cloneParameters(getParameters()));
    }
}
