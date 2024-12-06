package com.beprogramming.demo.processor.annotation.operator.logic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Exists extends LogicAbstract {
    public Exists(OperatorAbstract[] parameters) {
        super(parameters);
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("\\\\forall int i; ");
        OperatorAbstract[] allParameters = this.parameters[0].getParameters();
        buffer.append(allParameters[0]);
        buffer.append(";");
        buffer.append(allParameters[1]);
        return buffer.toString();
    }

    @Override
    public Object clone() {
        return new Exists(cloneParameters(getParameters()));
    }
}
