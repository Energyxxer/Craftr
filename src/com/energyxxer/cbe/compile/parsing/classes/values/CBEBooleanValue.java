package com.energyxxer.cbe.compile.parsing.classes.values;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenItem;
import com.energyxxer.cbe.compile.exceptions.IllegalOperandsException;
import com.energyxxer.cbe.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.cbe.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CBEBooleanValue extends CBEValue {

    private boolean value;

    public CBEBooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public String getInternalType() {
        return "boolean";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public CBEValue addition(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue subtraction(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue multiplication(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue division(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue modulo(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue exponentiate(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue increment() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CBEValue decrement() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThan(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThan(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) return false;
        CBEBooleanValue booleanOperand = (CBEBooleanValue) operand;
        return value == ((Boolean) booleanOperand.getValue());
    }

    @Override
    public CBEValue assignTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) throw new IllegalOperandsException();
        CBEBooleanValue booleanOperand = (CBEBooleanValue) operand;
        this.value = ((Boolean) booleanOperand.getValue());
        return this;
    }

    public static void init() {
        Evaluator.addEvaluator("BOOLEAN", p -> {
            if(p.getType().equals("ITEM")) {
                TokenItem item = (TokenItem) p;
                switch(item.getContents().value) {
                    case "true": {
                        return new CBEBooleanValue(true);
                    }
                    case "false": {
                        return new CBEBooleanValue(false);
                    }
                    default: {
                        Console.err.println("[ERROR] Boolean token with value \"" + item.getContents().value + "\"");
                        return null;
                    }
                }
            } else {
                Console.err.println("[ERROR] Boolean pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });
    }
}
