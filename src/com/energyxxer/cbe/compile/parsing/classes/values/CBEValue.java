package com.energyxxer.cbe.compile.parsing.classes.values;

import com.energyxxer.cbe.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public abstract class CBEValue {
    public abstract String getType();
    public abstract String getInternalType();
    public abstract Object getValue();

    //Arithmetic operations

    public abstract CBEValue addition               (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract CBEValue subtraction            (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract CBEValue multiplication         (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract CBEValue division               (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract CBEValue modulo                 (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract CBEValue exponentiate           (@NotNull CBEValue operand) throws IllegalOperandsException;

    public abstract CBEValue increment              () throws IllegalOperandsException;
    public abstract CBEValue decrement              () throws IllegalOperandsException;

    //Logical operations

    public abstract boolean isGreaterThan           (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract boolean isLessThan              (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract boolean isGreaterThanOrEqualTo  (@NotNull CBEValue operand) throws IllegalOperandsException;
    public abstract boolean isLessThanOrEqualTo     (@NotNull CBEValue operand) throws IllegalOperandsException;

    public abstract boolean isEqualTo               (@NotNull CBEValue operand) throws IllegalOperandsException;

    //Other

    public abstract CBEValue assignTo                (@NotNull CBEValue operand) throws IllegalOperandsException;

    @Override
    public String toString() {
        return getValue().toString();
    }

    public static void init() {
        CBEBooleanValue.init();
        CBEFloatValue.init();
        CBEIntegerValue.init();
        CBENullValue.init();
    }
}
