package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Quantifier;
import org.springframework.util.StringUtils;

public class QuantifierMapper extends AbstractMapper<Quantifier> {
    public QuantifierMapper(Quantifier operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        return OperatorAbstract.toOperator(StringUtils.capitalize(term.op().toString()), processSubArguments(term));
    }
}
