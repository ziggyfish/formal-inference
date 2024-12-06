package com.beprogramming.demo.processor.annotation.operator.arithmetic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Mul extends ArithmeticAbstract {

    public Mul(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " * ";
    }
    @Override
    public Object clone() {
        return new Mul(cloneParameters(params));
    }
}
