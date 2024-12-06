package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.logic.All;

public class Value extends VariableAbstract {

    public Value(String variableName) {
        super(variableName);
    }

    @Override
    public Object clone() {
        return new Value(variableName);
    }
}
