package com.beprogramming.demo.processor.annotation.operator.logic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class LogicAbstract extends OperatorAbstract {
    protected OperatorAbstract[] parameters;

    public LogicAbstract(OperatorAbstract[] parameters) {
        this.parameters = parameters;
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
