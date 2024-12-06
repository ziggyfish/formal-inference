package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Gt extends BooleanAbstract {

    public Gt(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " > ";
    }

    @Override
    public Object clone() {
        return new Gt(cloneParameters(getParameters()));
    }
}
