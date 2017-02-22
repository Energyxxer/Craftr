package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.LiteralEvaluators;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public abstract class CraftrValue {

    private boolean implicit = false;


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

    public boolean isImplicit() {
        return implicit;
    }

    public void setImplicit(boolean implicit) {
        this.implicit = implicit;
    }

    @Override
    public String toString() {
        return getInternalType() + " >> " + getValue();
    }


    public static void init() {
        LiteralEvaluators.registerEvaluators();
    }
}
