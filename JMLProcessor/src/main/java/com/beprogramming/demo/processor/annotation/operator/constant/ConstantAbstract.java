package com.beprogramming.demo.processor.annotation.operator.constant;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class ConstantAbstract extends OperatorAbstract {
    protected OperatorAbstract[] parameters;

    public ConstantAbstract(OperatorAbstract[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return parameters[0].toString();
    }

    @Override
    public OperatorAbstract[] getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(OperatorAbstract[] parameters) {
        this.parameters = parameters;
    }
}
