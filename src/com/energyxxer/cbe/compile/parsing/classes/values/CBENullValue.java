package com.energyxxer.cbe.compile.parsing.classes.values;

import com.energyxxer.cbe.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CBENullValue extends CBEValue {
    public CBENullValue() {
    }

    @Override
    public String getType() {
        return "null";
    }

    @Override
    public String getInternalType() {
        return "null";
    }

    @Override
    public Object getValue() {
        return null;
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
        return operand.getInternalType().equals("null");
    }

    @Override
    public CBEValue assignTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        throw new IllegalOperandsException();
    }
}
