package com.beprogramming.demo.processor.annotation.operator;

public class Empty extends OperatorAbstract {

    public Empty(Object... args) {

    }

    @Override
    public OperatorAbstract[] getParameters() {
        return new OperatorAbstract[]{};
    }

    @Override
    public void setParameters(OperatorAbstract[] parameters) {

    }
}
