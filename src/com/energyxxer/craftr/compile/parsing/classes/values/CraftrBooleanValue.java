package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrBooleanValue extends CraftrValue {

    private boolean value;

    public CraftrBooleanValue(boolean value) {
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
    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue increment() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public CraftrValue decrement() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    @Override
    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) return false;
        CraftrBooleanValue booleanOperand = (CraftrBooleanValue) operand;
        return value == ((Boolean) booleanOperand.getValue());
    }

    @Override
    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) throw new IllegalOperandsException();
        CraftrBooleanValue booleanOperand = (CraftrBooleanValue) operand;
        this.value = ((Boolean) booleanOperand.getValue());
        return this;
    }

    public static void init() {
        Evaluator.addEvaluator("BOOLEAN", p -> {
            if(p.getType().equals("ITEM")) {
                TokenItem item = (TokenItem) p;
                switch(item.getContents().value) {
                    case "true": {
                        return new CraftrBooleanValue(true);
                    }
                    case "false": {
                        return new CraftrBooleanValue(false);
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
