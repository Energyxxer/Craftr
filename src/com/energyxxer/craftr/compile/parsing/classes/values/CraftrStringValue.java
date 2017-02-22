package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 2/14/2017.
 */
public class CraftrStringValue extends CraftrValue {

    private String value = null;

    public CraftrStringValue(String value) {
        this.value = value;
    }

    public String getType() {
        return "String";
    }

    public String getInternalType() {
        return "String";
    }

    public Object getValue() {
        return value;
    }

    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public CraftrValue increment() throws IllegalOperandsException {
        return null;
    }

    public CraftrValue decrement() throws IllegalOperandsException {
        return null;
    }

    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }
}
