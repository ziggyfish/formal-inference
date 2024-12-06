package com.beprogramming.demo.processor.annotation.operator.arithmetic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Sub extends ArithmeticAbstract {

    public Sub(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " - ";
    }

    @Override
    public Object clone() {
        return new Sub(cloneParameters(params));
    }
}
