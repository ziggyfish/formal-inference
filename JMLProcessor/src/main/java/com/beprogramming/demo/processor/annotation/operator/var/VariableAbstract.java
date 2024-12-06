package com.beprogramming.demo.processor.annotation.operator.var;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class VariableAbstract extends OperatorAbstract {

    public String variableName;

    public VariableAbstract(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return variableName;
    }

    @Override
    public OperatorAbstract[] getParameters() {
        return new OperatorAbstract[]{};
    }

    @Override
    public void setParameters(OperatorAbstract[] parameters) {

    }
}
