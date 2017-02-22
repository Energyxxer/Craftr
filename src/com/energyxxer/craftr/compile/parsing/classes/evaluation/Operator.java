package com.energyxxer.craftr.compile.parsing.classes.evaluation;

/**
 * Created by User on 2/18/2017.
 */
public enum Operator {
    //Multiplicative
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    //Additive
    PLUS("+"),
    MINUS("-"),
    //Relational
    LESS_THAN_OR_EQUAL_TO("<="),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    GREATER_THAN(">"),
    //Equality
    IS_EQUAL_TO("=="),
    IS_NOT_EQUAL_TO("!="),
    //Bitwise AND
    AND("&"),
    //Bitwise OR
    OR("|"),
    //Logical AND
    QUICK_AND("&&"),
    //Logical OR
    QUICK_OR("||"),
    //Ternary
    TERNARY_HEADER("?"),
    TERNARY_SEPARATOR(":"),
    //Assignment
    ASSIGN("="),
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    MULTIPLY_ASSIGN("*="),
    DIVIDE_ASSIGN("/="),
    MODULO_ASSIGN("%=");

    public final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }
}
