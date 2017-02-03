package com.energyxxer.cbe.compile.parsing.classes.values;

import com.energyxxer.cbe.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CBEIntegerValue extends CBENumericValue {

    private int value;

    public CBEIntegerValue(int value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "int";
    }

    @Override
    public String getInternalType() {
        return "number:int";
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public float getRawValue() {
        return value;
    }

    @Override
    public CBEValue addition(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CBEIntegerValue(this.value + (int) numericOperand.getRawValue());
            case 1: return new CBEFloatValue(this.value + numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CBEValue subtraction(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CBEIntegerValue(this.value - (int) numericOperand.getRawValue());
            case 1: return new CBEFloatValue(this.value - numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CBEValue multiplication(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CBEIntegerValue(this.value * (int) numericOperand.getRawValue());
            case 1: return new CBEFloatValue(this.value * numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CBEValue division(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        try {
            return new CBEFloatValue(value / numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CBEFloatValue(Float.MAX_VALUE);
        }
    }

    @Override
    public CBEValue modulo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        try {
            return new CBEFloatValue(value % numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CBEFloatValue(Float.MAX_VALUE);
        }
    }

    @Override
    public CBEValue exponentiate(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number:int")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return new CBEFloatValue((float) Math.pow(value, (int) numericOperand.getRawValue()));
    }

    @Override
    public CBEValue increment() throws IllegalOperandsException {
        value++;
        return this;
    }

    @Override
    public CBEValue decrement() throws IllegalOperandsException {
        value--;
        return this;
    }

    @Override
    public boolean isGreaterThan(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return this.value > numericOperand.getRawValue();
    }

    @Override
    public boolean isLessThan(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return this.value < numericOperand.getRawValue();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return this.value >= numericOperand.getRawValue();
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return this.value <= numericOperand.getRawValue();
    }

    @Override
    public boolean isEqualTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) return false;
        CBENumericValue numericOperand = (CBENumericValue) operand;
        return this.getRawValue() == numericOperand.getRawValue();
    }

    @Override
    public CBEValue assignTo(@NotNull CBEValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CBENumericValue numericOperand = (CBENumericValue) operand;
        this.value = (int) numericOperand.getRawValue();
        return this;
    }

    public static void init() {}
}
