package com.beprogramming.demo.processor.annotation.operator.arithmetic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.bool.BooleanAbstract;

import java.util.Arrays;

public class Add extends ArithmeticAbstract {

    public Add(OperatorAbstract[] params) {
        super(params);

    }

    @Override
    public String getOperatorString() {
        return " + ";
    }

    @Override
    public Object clone() {
        return new Add(cloneParameters(params));
    }
}
