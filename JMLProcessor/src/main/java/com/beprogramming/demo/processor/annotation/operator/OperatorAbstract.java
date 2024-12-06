package com.beprogramming.demo.processor.annotation.operator;

import com.beprogramming.demo.processor.annotation.operator.arithmetic.Mul;
import com.beprogramming.demo.processor.annotation.operator.arithmetic.Add;
import com.beprogramming.demo.processor.annotation.operator.arithmetic.Sub;
import com.beprogramming.demo.processor.annotation.operator.bool.*;
import com.beprogramming.demo.processor.annotation.operator.constant.Int;
import com.beprogramming.demo.processor.annotation.operator.constant.NonNull;
import com.beprogramming.demo.processor.annotation.operator.func.Imp;
import com.beprogramming.demo.processor.annotation.operator.logic.All;
import com.beprogramming.demo.processor.annotation.operator.logic.Exists;
import com.beprogramming.demo.processor.annotation.operator.var.Arr;
import com.beprogramming.demo.processor.annotation.operator.func.Length;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.lang.reflect.Method;

abstract public class OperatorAbstract implements Serializable, Cloneable {

    public static class OperatorParameter {
        public final OperatorAbstract[] parameters;
        OperatorParameter(OperatorAbstract[] parameters) {
            this.parameters = parameters;
        }
    }

    public static OperatorAbstract toOperator(String operatorName, OperatorAbstract[] parameters) {
        try {
            Method m = OperatorAbstract.class.getDeclaredMethod(Strings.concat("create", operatorName), OperatorParameter.class);
            return (OperatorAbstract) m.invoke(null, new OperatorParameter(parameters));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static OperatorAbstract createAdd(OperatorParameter parameters) {
        return new Add(parameters.parameters);
    }

    public static OperatorAbstract createAnd(OperatorParameter parameters) {

        return new And(parameters.parameters);
    }

    public static OperatorAbstract createSub(OperatorParameter parameters) {

        return new Sub(parameters.parameters);
    }

    public static OperatorAbstract createGt(OperatorParameter parameters) {

        return new Gt(parameters.parameters);
    }

    public static OperatorAbstract createMul(OperatorParameter parameters) {

        return new Mul(parameters.parameters);
    }

    public static OperatorAbstract createArr(OperatorParameter parameters) {
        return new Arr(parameters.parameters);
    }

    public static OperatorAbstract createNot(OperatorParameter parameters) {
        return new Not(parameters.parameters);
    }

    public static OperatorAbstract createImp(OperatorParameter parameters) {
        return new Imp(parameters.parameters);
    }

    public static OperatorAbstract createOr(OperatorParameter parameters) {
        return new Or(parameters.parameters);
    }

    public static OperatorAbstract createGeq(OperatorParameter parameters) {
        return new Geq(parameters.parameters);
    }

    public static OperatorAbstract createLength(OperatorParameter parameters) {
        return new Length(parameters.parameters);
    }

    public static OperatorAbstract createLt(OperatorParameter parameters) {
        return new Lt(parameters.parameters);
    }

    public static OperatorAbstract createleq(OperatorParameter parameters) {
        return new Leq(parameters.parameters);
    }

    public static OperatorAbstract createLeq(OperatorParameter parameters) {
        return new Leq(parameters.parameters);
    }

    public static OperatorAbstract createAll(OperatorParameter parameters) {
        return new All(parameters.parameters);
    }

    public static OperatorAbstract createinInt(OperatorParameter parameters) {
        return new Int(parameters.parameters);
    }

    public static OperatorAbstract createExists(OperatorParameter parameters) {
        return new Exists(parameters.parameters);
    }

    public static OperatorAbstract createNonNull(OperatorParameter parameters) {
        return new NonNull();
    }

    public static OperatorAbstract createEq(OperatorParameter parameters) {
        return new Eq(parameters.parameters);
    }

    public static OperatorAbstract createTrue(OperatorParameter parameters) {
        return new True(parameters.parameters);
    }

    abstract public OperatorAbstract[] getParameters();
    abstract public void setParameters(OperatorAbstract[] parameters);

    @Override
    public Object clone() {
        System.out.println(this.getClass().getCanonicalName());
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public OperatorAbstract[] cloneParameters(OperatorAbstract[] parameters) {
        OperatorAbstract[] newParameters = new OperatorAbstract[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            newParameters[i] = (OperatorAbstract) parameters[i].clone();

        }
        return newParameters;
    }


}
