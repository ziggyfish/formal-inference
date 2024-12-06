package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

import java.util.Arrays;

public class Imp extends FunctionAbstract {

    public Imp(OperatorAbstract[] parameters) {
        super(null, parameters);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(params[0]);
        buffer.append(" ==> ");
        buffer.append(params[1]);
        return buffer.toString();
    }

    @Override
    public Object clone() {
        return new Imp(cloneParameters(getParameters()));
    }
}
