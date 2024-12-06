package com.beprogramming.demo.processor.annotation.operator.logic;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class All extends LogicAbstract {
    public All(OperatorAbstract[] parameters) {
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
        return new All(cloneParameters(getParameters()));
    }
}
