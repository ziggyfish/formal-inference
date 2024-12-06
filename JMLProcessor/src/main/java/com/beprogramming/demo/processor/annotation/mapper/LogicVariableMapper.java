package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.LogicVariable;

public class LogicVariableMapper extends AbstractMapper<LogicVariable> {
    public LogicVariableMapper(LogicVariable operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        return new com.beprogramming.demo.processor.annotation.operator.var.LogicVariable(operator.name().toString(), processSubArguments(term));
    }
}
