package com.beprogramming.demo.processor.annotation.mapper;

import com.beprogramming.demo.processor.annotation.operator.*;
import com.beprogramming.demo.processor.annotation.operator.var.Boolean;
import com.beprogramming.demo.processor.annotation.operator.var.Field;
import com.beprogramming.demo.processor.annotation.operator.constant.Null;
import com.beprogramming.demo.processor.annotation.operator.var.Value;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Function;
import org.springframework.util.StringUtils;

public class FunctionMapper extends AbstractMapper<Function> {
    public FunctionMapper(Function operator) {
        super(operator);
    }

    @Override
    public OperatorAbstract toAnnotation(Term term) throws ClassNotFoundException {

        if (term.isRigid()) {
            return switch (term.sort().toString()) {
                case "int" -> new Value(term.sub(0).op().name().toString());
                case "Field" -> new Field(operator.name().toString(), processSubArguments(term));
                case "Null" -> new Null();
                case "boolean" -> new Boolean(operator.name().toString().toLowerCase());
                case "Formula" -> OperatorAbstract.toOperator(term.op().name().toString(), processSubArguments(term));
                case "LocSet" -> new Empty();
                default -> new Unknown(operator.name().toString(), term.sort().toString());
            };

        }


        return switch (term.sort().toString()) {
            default ->
                    OperatorAbstract.toOperator(StringUtils.capitalize(operator.toString()), processSubArguments(term));
        };
    }
}
