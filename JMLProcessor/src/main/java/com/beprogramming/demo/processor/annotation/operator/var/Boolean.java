package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.logic.All;

public class Boolean extends VariableAbstract {

    public Boolean(String variableName) {
        super(variableName);
    }

    @Override
    public Object clone() {
        return new Boolean(variableName);
    }
}
