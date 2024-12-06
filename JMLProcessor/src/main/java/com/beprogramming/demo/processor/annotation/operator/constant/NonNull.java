package com.beprogramming.demo.processor.annotation.operator.constant;

import com.beprogramming.demo.processor.annotation.operator.var.VariableAbstract;

public class NonNull extends VariableAbstract {
    public NonNull() {
        super(null);
    }

    @Override
    public Object clone() {
        return new NonNull();
    }
}
