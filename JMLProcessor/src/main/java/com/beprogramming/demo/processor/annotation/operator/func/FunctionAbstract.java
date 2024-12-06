package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;

public class FunctionAbstract extends OperatorAbstract {
    protected OperatorAbstract[] params;
    protected String functionName;
    public FunctionAbstract(String functionName, OperatorAbstract[] params){
        this.params = params;
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        ArrayList<String> args = new ArrayList<>();
        for (OperatorAbstract op : params){
            args.add(op.toString());
        }

        return Strings.concat(functionName, Strings.concat(Strings.concat("(", String.join(",", args)), ")"));
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
