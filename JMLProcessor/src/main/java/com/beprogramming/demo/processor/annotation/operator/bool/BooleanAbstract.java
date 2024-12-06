package com.beprogramming.demo.processor.annotation.operator.bool;

import com.beprogramming.demo.processor.Utils;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;

import java.util.ArrayList;

abstract public class BooleanAbstract extends OperatorAbstract {
    protected OperatorAbstract[] params;

    public BooleanAbstract(OperatorAbstract[] params) {
        this.params = params;
    }

    abstract public String getOperatorString();

    @Override
    public String toString() {

        if (params.length == 1){
            return Utils.concat(getOperatorString(), params[0].toString());
        }

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

    public void setParams(OperatorAbstract[] params) {
        this.params = params;
    }
}
