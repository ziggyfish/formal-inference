package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Not extends BooleanAbstract {

    public Not(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return "!";
    }

    @Override
    public Object clone() {
        return new Not(cloneParameters(getParameters()));
    }
}
