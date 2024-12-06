package com.beprogramming.demo.processor.annotation.operator.constant;

import com.beprogramming.demo.processor.annotation.operator.var.VariableAbstract;

public class Null extends VariableAbstract {
    public Null() {
        super(null);
    }

    @Override
    public Object clone() {
        return new Null();
    }
}
