package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Length extends FunctionAbstract {
    public Length(OperatorAbstract[] params){

        super("Length", params);
    }

    @Override
    public Object clone() {
        return new Length(cloneParameters(getParameters()));
    }
}
