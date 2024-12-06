package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class And extends BooleanAbstract {

    public And(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " && ";
    }

    @Override
    public Object clone() {
        return new And(cloneParameters(getParameters()));
    }
}
