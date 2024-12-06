package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.logic.All;

public class LocalVar extends VariableAbstract {

    public LocalVar(String variableName) {
        super(variableName);
    }
    @Override
    public Object clone() {
        return new LocalVar(variableName);
    }
}
