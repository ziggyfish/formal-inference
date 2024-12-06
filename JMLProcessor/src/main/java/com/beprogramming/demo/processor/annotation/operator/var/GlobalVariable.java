package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.logic.All;

public class GlobalVariable extends VariableAbstract {
    private String qualifier;

    public GlobalVariable(String qualifier, String name) {
        super(name);
        this.qualifier = qualifier;
    }

    @Override
    public Object clone() {
        return new GlobalVariable(qualifier, variableName);
    }
}
