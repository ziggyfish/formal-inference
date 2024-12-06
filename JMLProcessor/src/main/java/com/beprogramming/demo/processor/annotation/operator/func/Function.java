package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.bool.Eq;

public class Function extends FunctionAbstract {

    public Function(String functionName, OperatorAbstract[] parameters) {

        super(functionName, parameters);
    }

    @Override
    public Object clone() {
        return new Function(functionName, cloneParameters(getParameters()));
    }
}
