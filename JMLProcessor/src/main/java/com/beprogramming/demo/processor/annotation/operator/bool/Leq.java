package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Leq extends BooleanAbstract {

    public Leq(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " <= ";
    }

    @Override
    public Object clone() {
        return new Leq(cloneParameters(getParameters()));
    }
}
