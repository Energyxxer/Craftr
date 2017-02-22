package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrBooleanValue extends CraftrValue {

    private boolean value;

    public CraftrBooleanValue(boolean value) {
        this.value = value;
    }

    public String getType() {
        return "boolean";
    }

    public String getInternalType() {
        return "boolean";
    }

    public Object getValue() {
        return value;
    }

    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue increment() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public CraftrValue decrement() throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }

    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) return false;
        CraftrBooleanValue booleanOperand = (CraftrBooleanValue) operand;
        return value == ((Boolean) booleanOperand.getValue());
    }

    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().equals("boolean")) throw new IllegalOperandsException();
        CraftrBooleanValue booleanOperand = (CraftrBooleanValue) operand;
        this.value = ((Boolean) booleanOperand.getValue());
        return this;
    }
}
