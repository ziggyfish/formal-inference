package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Junctor;
import org.springframework.util.StringUtils;

public class JunctorMapper extends AbstractMapper<Junctor> {
    public JunctorMapper(Junctor operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        return OperatorAbstract.toOperator(StringUtils.capitalize(operator.toString()), processSubArguments(term));
    }
}
