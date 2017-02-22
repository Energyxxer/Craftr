package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrIntegerValue extends CraftrNumericValue {

    private int value;

    public CraftrIntegerValue(int value) {
        this.value = value;
    }

    public String getType() {
        return "int";
    }

    public String getInternalType() {
        return "number:int";
    }

    public int getWeight() {
        return 0;
    }

    public Object getValue() {
        return value;
    }

    public float getRawValue() {
        return value;
    }

    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value + (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value + numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value - (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value - numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value * (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value * numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        try {
            return new CraftrFloatValue(value / numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CraftrFloatValue(Float.MAX_VALUE);
        }
    }

    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        try {
            return new CraftrFloatValue(value % numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CraftrFloatValue(Float.MAX_VALUE);
        }
    }

    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number:int")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return new CraftrFloatValue((float) Math.pow(value, (int) numericOperand.getRawValue()));
    }

    public CraftrValue increment() throws IllegalOperandsException {
        value++;
        return this;
    }

    public CraftrValue decrement() throws IllegalOperandsException {
        value--;
        return this;
    }

    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value > numericOperand.getRawValue();
    }

    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value < numericOperand.getRawValue();
    }

    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value >= numericOperand.getRawValue();
    }

    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value <= numericOperand.getRawValue();
    }

    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) return false;
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.getRawValue() == numericOperand.getRawValue();
    }

    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        this.value = (int) numericOperand.getRawValue();
        return this;
    }
}
