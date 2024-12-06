package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.logic.All;

public class Field extends VariableAbstract {
    private OperatorAbstract[] context;
    public Field(String varName, OperatorAbstract[] context){

        super(varName);
        this.context = context;
    }

    @Override
    public Object clone() {
        return new Field(variableName, cloneParameters(getParameters()));
    }
}
