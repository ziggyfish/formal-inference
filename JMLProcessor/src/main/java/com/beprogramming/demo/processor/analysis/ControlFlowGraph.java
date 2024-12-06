package com.beprogramming.demo.processor.analysis;

import com.beprogramming.demo.processor.JMLAnnotationProcessor;
import com.beprogramming.demo.processor.annotation.operator.Empty;
import com.beprogramming.demo.processor.annotation.operator.OperatorAbstract;
import com.beprogramming.demo.processor.annotation.operator.arithmetic.Add;
import com.beprogramming.demo.processor.annotation.operator.arithmetic.Sub;
import com.beprogramming.demo.processor.annotation.operator.bool.*;
import com.beprogramming.demo.processor.annotation.operator.func.Imp;
import com.beprogramming.demo.processor.annotation.operator.var.Value;
import com.beprogramming.demo.processor.annotation.operator.var.Variable;
import com.sun.source.tree.*;
import com.sun.source.util.Trees;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.*;

import com.beprogramming.demo.processor.Utils;

public class ControlFlowGraph {

    private final Trees tree;
    public final Map<String, ProgramVariable> variables = new HashMap<>();
    private final CompilationUnitTree cu;
    public final Stack<OperatorAbstract> conditions = new Stack<>();
    public final Map<String, Map<String, OperatorAbstract>> specifications;
    public final String currentMethod;
    public final String currentClass;

    public ControlFlowGraph(MethodTree methodTree, Trees tree, CompilationUnitTree cu, Map<String, Map<String, OperatorAbstract>> specifications, Map<String, ProgramVariable> variables) {
        super();
        this.tree = tree;
        this.cu = cu;
        this.specifications = specifications;
        ExecutableElement element = (ExecutableElement)tree.getElement(tree.getPath(cu, methodTree));
        this.currentMethod = Utils.getMethodName(element);
        this.currentClass = Utils.getClass(element);
        for (Map.Entry<String, ProgramVariable> var : variables.entrySet()) {
            this.variables.put(var.getKey(), var.getValue().copy());
        }
    }



    public void processAssignment(AssignmentTree assignmentNode) {
        IdentifierTree identifierTree = (IdentifierTree) assignmentNode.getVariable();

        //System.out.println(identifierTree);
        //System.out.println(assignmentNode.getExpression());

        ProgramVariable var = variables.get(identifierTree.getName().toString());

        OperatorAbstract expression = expressionToOperatorAbstract(assignmentNode.getExpression());

        var.setCurrentExpression(substituteVariable(expression));
    }
    public OperatorAbstract substituteVariable(OperatorAbstract currentNode) {
        return substituteVariable(currentNode, variables);
    }

    public OperatorAbstract substituteVariable(OperatorAbstract currentNode, Map<String, ProgramVariable> variables) {
        if (currentNode instanceof Variable var) {

            ProgramVariable variable = variables.get(var.variableName);

            if (variable == null) {
                return currentNode;
            }

            OperatorAbstract value = variable.getCurrentExpression();

            if (value != null) {
                return (OperatorAbstract) value.clone();
            } else {
                return currentNode;
            }
        } else {
            OperatorAbstract[] parameters = currentNode.getParameters();
            for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
                OperatorAbstract op = parameters[i];

                parameters[i] = (OperatorAbstract) substituteVariable(op, variables).clone();

            }
            return currentNode;
        }


    }

    public void processVariable(VariableTree node) {
        VariableElement el = (VariableElement) tree.getElement(tree.getPath(cu, node));
        ExpressionTree init = node.getInitializer();

        ProgramVariable var = new ProgramVariable(node, el.getKind(), expressionToOperatorAbstract(init));

        if (init != null) {
            var.setCurrentExpression(substituteVariable(var.getCurrentExpression(), variables));
        }

        variables.put(var.name, var);
    }

    public OperatorAbstract expressionToOperatorAbstract(ExpressionTree expression) {
        if (expression == null)
            return null;

        switch(expression.getKind()) {
            case PLUS -> {
                BinaryTree binaryExpression = (BinaryTree) expression;
                return new Add(new OperatorAbstract[]{
                        expressionToOperatorAbstract(binaryExpression.getLeftOperand()),
                        expressionToOperatorAbstract(binaryExpression.getRightOperand())
                });
            }

            case MULTIPLY -> {
                BinaryTree binaryExpression = (BinaryTree) expression;
                return new com.beprogramming.demo.processor.annotation.operator.arithmetic.Mul(new OperatorAbstract[]{
                        expressionToOperatorAbstract(binaryExpression.getLeftOperand()),
                        expressionToOperatorAbstract(binaryExpression.getRightOperand())
                });
            }
            case IDENTIFIER -> {
                IdentifierTree identifierTree = (IdentifierTree) expression;
                return new Variable(identifierTree.getName().toString());
            }
            case MINUS -> {
                BinaryTree binaryExpression = (BinaryTree) expression;
                return new Sub(new OperatorAbstract[]{
                        expressionToOperatorAbstract(binaryExpression.getLeftOperand()),
                        expressionToOperatorAbstract(binaryExpression.getRightOperand())
                });
            }
            case GREATER_THAN -> {
                BinaryTree binaryExpression = (BinaryTree) expression;
                return new com.beprogramming.demo.processor.annotation.operator.bool.Gt(new OperatorAbstract[]{
                        expressionToOperatorAbstract(binaryExpression.getLeftOperand()),
                        expressionToOperatorAbstract(binaryExpression.getRightOperand())
                });
            }
            case INT_LITERAL -> {
                LiteralTree literalTree = (LiteralTree) expression;
                return new Value(literalTree.getValue().toString());
            }
            case PARENTHESIZED -> {
                ParenthesizedTree parenthesizedTree = (ParenthesizedTree) expression;

                return expressionToOperatorAbstract(parenthesizedTree.getExpression());
            }

            case EQUAL_TO -> {
                BinaryTree binaryExpression = (BinaryTree) expression;
                return new com.beprogramming.demo.processor.annotation.operator.bool.Eq(new OperatorAbstract[]{
                        expressionToOperatorAbstract(binaryExpression.getLeftOperand()),
                        expressionToOperatorAbstract(binaryExpression.getRightOperand())
                });
            }
            case METHOD_INVOCATION -> {
                MethodInvocationTree methodInvocationTree = (MethodInvocationTree) expression;

                ExecutableElement element = (ExecutableElement)tree.getElement(tree.getPath(cu, methodInvocationTree));
                String methodName = Utils.getMethodName(element);
                System.out.println("================================================dddddd");
                if (specifications.get(currentMethod) == null)
                    specifications.put(currentMethod, new HashMap<>());
                OperatorAbstract requires = getSubstitutedSpecification(methodInvocationTree, methodName, "requires");

                if (specifications.get(currentMethod).get("requires") == null) {
                    specifications.get(currentMethod).put("requires", requires);
                } else {
                    OperatorAbstract oldRequires = specifications.get(currentMethod).get("requires");
                    specifications.get(currentMethod).put("requires", new And(new OperatorAbstract[]{oldRequires, requires}));
                }


                return getSubstitutedSpecification(methodInvocationTree, methodName, "ensures");
            }
            default -> {
                System.out.println(expression.getClass().getCanonicalName());
                System.out.println(expression.getKind());
            }
        }
        return null;
    }

    public OperatorAbstract getSubstitutedSpecification(MethodInvocationTree node, String currentMethod, String specificationType) {

        ExecutableElement element = (ExecutableElement)tree.getElement(tree.getPath(cu, node));
        String methodName = Utils.getMethodName(element);
        Map<String, OperatorAbstract> methodSpec = specifications.get(methodName);

        if (methodSpec == null)
            return null;

        if (!specifications.containsKey(currentMethod)) {
            specifications.put(currentMethod, new HashMap<>());
        }

        List<? extends ExpressionTree> arguments = node.getArguments();
        List<? extends VariableElement> parameters = element.getParameters();

        Map<String, ControlFlowGraph.ProgramVariable> substitutedElements = new HashMap<>();
        for (int i = 0; i < arguments.size(); i++) {

            String parameterName = parameters.get(i).getSimpleName().toString();
            OperatorAbstract expression = substituteVariable(expressionToOperatorAbstract(arguments.get(i)), variables);
            ControlFlowGraph.ProgramVariable newVar = new ControlFlowGraph.ProgramVariable(parameterName, parameters.get(i).getKind(),expression);
            substitutedElements.put(parameterName, newVar);

        }

        Map<String, OperatorAbstract> currentMethodSpec = specifications.get(currentMethod);
        Map<String, OperatorAbstract> callingMethodSpec = specifications.get(methodName);
        if (currentMethodSpec == null) {
            currentMethodSpec = new HashMap<>();
        }

        if (callingMethodSpec == null) {
            return new True(new OperatorAbstract[0]);
        }

        OperatorAbstract ensures = callingMethodSpec.get(specificationType);

        if (!conditions.isEmpty()) {

            ensures = new Imp(new OperatorAbstract[]{ getCurrentConditions(), ensures});
        }

        if (ensures != null) {


            OperatorAbstract newEnsures = substituteVariable((OperatorAbstract) ensures.clone(), substitutedElements);

            OperatorAbstract oldRequires = currentMethodSpec.get(specificationType);
            if (oldRequires == null) {
                return newEnsures;
            } else {
                return (OperatorAbstract) newEnsures.clone();
            }
        }

        return currentMethodSpec.get(specificationType);
    }

    public void processIf(IfTree node, JMLAnnotationProcessor.TreePathScannerToJavaPoet treePathScannerToJavaPoet) {
        OperatorAbstract condition = expressionToOperatorAbstract(node.getCondition());

        conditions.push(condition);

        treePathScannerToJavaPoet.scan(node.getThenStatement(), null);
        conditions.pop();

        if (node.getElseStatement() != null) {
            OperatorAbstract notCondition = new Not(new OperatorAbstract[]{condition});
            System.out.println("===================1111");

            System.out.println(notCondition);
            conditions.push(notCondition);
            treePathScannerToJavaPoet.scan(node.getElseStatement(), null);
            conditions.pop();
        }

    }

    public OperatorAbstract getCurrentConditions() {
        if (conditions.isEmpty()) {
            return null;
        }

        OperatorAbstract[] conditionsArray = conditions.toArray(new OperatorAbstract[0]);

        if (conditionsArray.length == 1) {
            return conditionsArray[0];
        }

        return new And(conditionsArray);
    }

    public void processAssignments() {
        for (Map.Entry<String, ProgramVariable> entry : variables.entrySet()) {
            ProgramVariable var = entry.getValue();
            if (var.type == ElementKind.FIELD) {
                if (var.isAssigned) {
                    OperatorAbstract newEnsures = new Eq(new OperatorAbstract[]{new Variable(Utils.concat(currentClass, ".", var.name)), var.currentExpression});
                    HashMap<String, OperatorAbstract> currentSpec = (HashMap<String, OperatorAbstract>) specifications.getOrDefault(currentMethod, new HashMap<>());
                    if (currentSpec.get("ensures") == null) {

                        currentSpec.put("ensures", newEnsures);
                    } else {
                        currentSpec.put("ensures", getOptimizedAnd(currentSpec.get("ensures"), newEnsures));
                    }
                    specifications.put(currentMethod, currentSpec);
                }
            }
        }
    }

    public OperatorAbstract getOptimizedAnd(OperatorAbstract... operators) {

        List<OperatorAbstract> ands = new ArrayList<>();
        for (OperatorAbstract operator : operators) {
            if (operator == null)
                continue;
            ands.add(operator);
        }

        if (ands.isEmpty())
            return new Empty();
        if (ands.size() == 1)
            return ands.get(0);

        return new And(ands.toArray(new OperatorAbstract[0]));
    }

    public OperatorAbstract getOptimizedOr(OperatorAbstract... operators) {
        List<OperatorAbstract> ands = new ArrayList<>();
        for (OperatorAbstract operator : operators) {
            if (operator == null)
                continue;
            ands.add(operator);
        }

        if (ands.isEmpty())
            return new Empty();
        if (ands.size() == 1)
            return ands.get(0);

        return new Or(ands.toArray(new OperatorAbstract[0]));
    }

    public static class ProgramVariable {

        public String name;
        public ElementKind type;
        public Boolean isAssigned = false;

        private OperatorAbstract currentExpression;


        public ProgramVariable(VariableTree variableTree, ElementKind type, OperatorAbstract currentExpression) {
            this.name = variableTree.getName().toString();
            this.type = type;
            this.currentExpression = currentExpression;
        }
        public ProgramVariable(String name, ElementKind type, OperatorAbstract currentExpression) {
            this.name = name;
            this.type = type;
            this.currentExpression = currentExpression;
        }

        public String toString() {

            return Utils.concat(name, " : ", type, " = ", getCurrentExpression());
        }

        public OperatorAbstract getCurrentExpression() {
            return currentExpression;
        }

        public void setCurrentExpression(OperatorAbstract currentExpression) {
            this.isAssigned = true;
            this.currentExpression = currentExpression;
        }

        public ProgramVariable copy() {
            return new ProgramVariable(name, type, currentExpression);
        }
    }
}
