package com.beprogramming.demo.processor.annotation.operator.constant;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.bool.Eq;

public class Int extends ConstantAbstract {
    public Int(OperatorAbstract[] parameters) {
        super(parameters);
    }

    @Override
    public Object clone() {
        return new Int(cloneParameters(getParameters()));
    }

}
