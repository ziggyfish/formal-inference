package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

public class Or extends BooleanAbstract {
    public Or(OperatorAbstract[] params) {
        super(params);
    }

    @Override
    public String getOperatorString() {
        return " || ";
    }

    @Override
    public Object clone() {
        return new Or(cloneParameters(getParameters()));
    }
}
