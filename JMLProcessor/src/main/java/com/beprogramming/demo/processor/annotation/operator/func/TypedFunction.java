package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class TypedFunction extends FunctionAbstract {
    protected String type;

    public TypedFunction(String functionName, String Type, OperatorAbstract[] params) {

        super(functionName, params);
        this.type = Type;
    }

    @Override
    public Object clone() {
        return new TypedFunction(functionName, type, cloneParameters(getParameters()));
    }
}
