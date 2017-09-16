package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

public enum Operator {
    //USED ON OPERATIONS
    INCREMENT("++", 0),
    DECREMENT("--", 0),
    NOT("!", 0),
    MULTIPLY("*", 1),
    DIVIDE("/", 1),
    MODULO("%", 1),
    ADD("+", 2),
    SUBTRACT("-", 2),
    LESS_THAN("<", 3),
    LESS_THAN_OR_EQUAL("<=", 3),
    GREATER_THAN(">", 3),
    GREATER_THAN_OR_EQUAL(">=", 3),
    EQUAL("==", 4),
    AND("&&", 5),
    OR("||", 6),
    //USED BY THE EXPRESSION PARSER INSTEAD OF THE EXPRESSION HANDLER
    INSTANCEOF("instanceof", 3),
    NOT_EQUAL("!=", 4),
    ASSIGN("=", 8, true),
    ADD_THEN_ASSIGN("+=", 8, true),
    SUBTRACT_THEN_ASSIGN("-=", 8, true),
    MULTIPLY_THEN_ASSIGN("*=", 8, true),
    DIVIDE_THEN_ASSIGN("/=", 8, true),
    MODULO_THEN_ASSIGN("%=", 8, true),
    AND_THEN_ASSIGN("&=", 8, true),
    OR_THEN_ASSIGN("|=", 8, true);

    private String symbol;
    private int precedence;
    private boolean rtl = false;

    Operator(String symbol, int precedence) {
        this.symbol = symbol;
        this.precedence = precedence;
    }

    Operator(String symbol, int precedence, boolean rtl) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.rtl = rtl;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isRightToLeft() {
        return rtl;
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
                ", rtl=" + rtl +
                '}';
    }
}
