package com.energyxxer.craftr.compile.parsing.classes.evaluation;

/**
 * Created by User on 2/18/2017.
 */
public enum UnaryOperator {
    IDENTITY("+"),
    NUMERICAL_NEGATION("-"),
    LOGICAL_NEGATION("!"),
    INCREMENT("++"),
    DECREMENT("--");

    public final String symbol;

    UnaryOperator(String symbol) {
        this.symbol = symbol;
    }
}
