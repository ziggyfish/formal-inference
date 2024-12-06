package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class True extends BooleanAbstract {

    public True(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " True ";
    }

    @Override
    public Object clone() {
        return new True(cloneParameters(getParameters()));
    }

}
