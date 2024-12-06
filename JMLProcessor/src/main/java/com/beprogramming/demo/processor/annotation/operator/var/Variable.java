package com.beprogramming.demo.processor.annotation.operator.var;

public class Variable extends VariableAbstract {

    public Variable(String variableName) {
        super(variableName);

    }

    @Override
    public Object clone()  {
        return new Variable(variableName);
    }
}
