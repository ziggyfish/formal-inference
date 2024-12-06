package com.beprogramming.demo.processor.annotation.operator;

public class Unknown extends OperatorAbstract {

    public Unknown(Object... args) {
        System.out.print("Unknown operator: ");
        System.out.print(args[0]);
        System.exit(1);
    }

    @Override
    public OperatorAbstract[] getParameters() {
        return new OperatorAbstract[]{};
    }

    @Override
    public void setParameters(OperatorAbstract[] parameters) {

    }
}
