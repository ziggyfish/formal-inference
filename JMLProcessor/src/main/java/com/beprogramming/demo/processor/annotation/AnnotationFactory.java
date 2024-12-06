package com.beprogramming.demo.processor.annotation;

import com.beprogramming.demo.processor.annotation.mapper.*;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.*;



public class AnnotationFactory {
    public static AbstractMapper getMapper(Term t) {

        de.uka.ilkd.key.logic.op.Operator op = t.op();

        if (op instanceof ObserverFunction p)
            return new ObserverFunctionMapper(p);

        if (op instanceof SortDependingFunction p)
            return new SortDependingFunctionMapper(p);

        if (op instanceof Function p)
            return new FunctionMapper((Function) op);

        if (op instanceof LocationVariable)
            return new LocationVariableMapper((LocationVariable) op);

        if (op instanceof Junctor)
            return new JunctorMapper((Junctor) op);

        if (op instanceof Quantifier)
            return new QuantifierMapper((Quantifier) op);

        if (op instanceof LogicVariable)
            return new LogicVariableMapper((LogicVariable) op);

        if (op instanceof Equality)
            return new EqualityMapper((Equality) op);

        StringBuffer b = new StringBuffer();
        b.append(op.getClass().getCanonicalName());
        b.append(" Unknown");
        throw new RuntimeException(b.toString());

    }
}
