package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public abstract class CraftrValue {
    public abstract String getType();
    public abstract String getInternalType();
    public abstract Object getValue();

    //Arithmetic operations

    public abstract CraftrValue addition               (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract CraftrValue subtraction            (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract CraftrValue multiplication         (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract CraftrValue division               (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract CraftrValue modulo                 (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract CraftrValue exponentiate           (@NotNull CraftrValue operand) throws IllegalOperandsException;

    public abstract CraftrValue increment              () throws IllegalOperandsException;
    public abstract CraftrValue decrement              () throws IllegalOperandsException;

    //Logical operations

    public abstract boolean isGreaterThan           (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract boolean isLessThan              (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract boolean isGreaterThanOrEqualTo  (@NotNull CraftrValue operand) throws IllegalOperandsException;
    public abstract boolean isLessThanOrEqualTo     (@NotNull CraftrValue operand) throws IllegalOperandsException;

    public abstract boolean isEqualTo               (@NotNull CraftrValue operand) throws IllegalOperandsException;

    //Other

    public abstract CraftrValue assignTo                (@NotNull CraftrValue operand) throws IllegalOperandsException;

    @Override
    public String toString() {
        return getValue().toString();
    }

    public static void init() {
        CraftrBooleanValue.init();
        CraftrFloatValue.init();
        CraftrIntegerValue.init();
        CraftrNullValue.init();
    }
}
