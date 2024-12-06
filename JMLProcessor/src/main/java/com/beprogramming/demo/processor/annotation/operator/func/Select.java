package com.beprogramming.demo.processor.annotation.operator.func;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import org.apache.logging.log4j.util.Strings;

public class Select extends TypedFunction {
    public Select(String functionName, String Type, OperatorAbstract[] params) {
        super(functionName, Type, params);
    }

    @Override
    public String toString() {
        return Strings.concat(params[1].toString(), Strings.concat(".", params[2].toString()));
    }

    @Override
    public Object clone() {
        return new Select(functionName,type, cloneParameters(getParameters()));
    }
}
