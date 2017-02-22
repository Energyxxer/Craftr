package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrNullValue extends CraftrValue {
    public CraftrNullValue() {
    }

    public String getType() {
        return "null";
    }

    public String getInternalType() {
        return "null";
    }

    public Object getValue() {
        return null;
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
        return operand.getInternalType().equals("null");
    }

    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }
}
