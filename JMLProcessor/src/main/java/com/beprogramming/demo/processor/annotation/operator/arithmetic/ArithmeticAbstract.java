package com.beprogramming.demo.processor.annotation.operator.arithmetic;

import com.beprogramming.demo.processor.Utils;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

import java.util.ArrayList;

abstract public class ArithmeticAbstract extends OperatorAbstract {
    protected OperatorAbstract[] params;

    public ArithmeticAbstract(OperatorAbstract[] params) {
        this.params = params;
    }

    abstract public String getOperatorString();

    @Override
    public String toString() {

        ArrayList<String> args = new ArrayList<>();
        for (OperatorAbstract op : params){
            args.add(op.toString());
        }

        return Utils.concat("(", String.join(getOperatorString(), args), ")");
    }

    @Override
    public OperatorAbstract[] getParameters() {
        return params;
    }

    @Override
    public void setParameters(OperatorAbstract[] parameters) {
        this.params = parameters;
    }
}
