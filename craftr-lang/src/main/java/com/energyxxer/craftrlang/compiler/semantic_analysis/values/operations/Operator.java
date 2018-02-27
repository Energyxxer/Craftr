package com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperandType.REFERENCE;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperandType.VALUE;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperationOrder.LTR;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperationOrder.RTL;
import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperatorType.*;

public enum Operator {
    //USED ON OPERATIONS
    INCREMENT("++", 0, UNARY_ANY, LTR, REFERENCE),
    DECREMENT("--", 0, UNARY_ANY, LTR, REFERENCE),
    NOT("!", 0, UNARY_LEFT, RTL, VALUE),
    MULTIPLY("*", 1, BINARY, LTR, VALUE),
    DIVIDE("/", 1, BINARY, LTR, VALUE),
    MODULO("%", 1, BINARY, LTR, VALUE),
    ADD("+", 2, BINARY, LTR, VALUE),
    SUBTRACT("-", 2, BINARY, LTR, VALUE),
    LESS_THAN("<", 3, BINARY, LTR, VALUE),
    LESS_THAN_OR_EQUAL("<=", 3, BINARY, LTR, VALUE),
    GREATER_THAN(">", 3, BINARY, LTR, VALUE),
    GREATER_THAN_OR_EQUAL(">=", 3, BINARY, LTR, VALUE),
    EQUAL("==", 4, BINARY, LTR, VALUE),
    NOT_EQUAL("!=", 4, BINARY, LTR, VALUE),
    AND("&&", 5, BINARY, LTR, VALUE),
    OR("||", 6, BINARY, LTR, VALUE),
    //USED BY THE EXPRESSION PARSER INSTEAD OF THE EXPRESSION HANDLER
    INSTANCEOF("instanceof", 3, BINARY, LTR, REFERENCE),
    ASSIGN("=", 8, BINARY, RTL, REFERENCE),
    ADD_THEN_ASSIGN("+=", 8, BINARY, RTL, REFERENCE),
    SUBTRACT_THEN_ASSIGN("-=", 8, BINARY, RTL, REFERENCE),
    MULTIPLY_THEN_ASSIGN("*=", 8, BINARY, RTL, REFERENCE),
    DIVIDE_THEN_ASSIGN("/=", 8, BINARY, RTL, REFERENCE),
    MODULO_THEN_ASSIGN("%=", 8, BINARY, RTL, REFERENCE),
    AND_THEN_ASSIGN("&=", 8, BINARY, RTL, REFERENCE),
    OR_THEN_ASSIGN("|=", 8, BINARY, RTL, REFERENCE);

    private String symbol;
    private int precedence;
    private OperationOrder order;
    private OperatorType operatorType;
    private OperandType leftOperandType;
    private OperandType rightOperandType;

    Operator(String symbol, int precedence, OperatorType operatorType, OperationOrder order, OperandType leftOperandType) {
        this(symbol, precedence, operatorType, order, leftOperandType, VALUE);
    }

    Operator(String symbol, int precedence, OperatorType operatorType, OperationOrder order, OperandType leftOperandType, OperandType rightOperandType) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.operatorType = operatorType;
        this.order = order;
        this.leftOperandType = leftOperandType;
        this.rightOperandType = rightOperandType;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public OperationOrder getOrder() {
        return order;
    }

    public OperandType getLeftOperandType() {
        return leftOperandType;
    }

    public OperandType getRightOperandType() {
        return rightOperandType;
    }

    public static Operator getOperatorForSymbol(String symbol) {
        for(Operator op : values())
            if(op.symbol.equals(symbol)) return op;
        return null;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "symbol='" + symbol + '\'' +
                ", precedence=" + precedence +
                ", order=" + order +
                '}';
    }
}
