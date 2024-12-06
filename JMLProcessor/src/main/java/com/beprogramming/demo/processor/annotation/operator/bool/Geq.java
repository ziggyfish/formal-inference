package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Geq extends BooleanAbstract {

    public Geq(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " >= ";
    }

    @Override
    public Object clone() {
        return new Geq(cloneParameters(getParameters()));
    }
}
