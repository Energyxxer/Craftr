package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrIntegerValue extends CraftrNumericValue {

    private int value;

    public CraftrIntegerValue(int value) {
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
    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value + (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value + numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value - (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value - numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        switch(numericOperand.getWeight()) {
            case 0: return new CraftrIntegerValue(this.value * (int) numericOperand.getRawValue());
            case 1: return new CraftrFloatValue(this.value * numericOperand.getRawValue());
            default: throw new IllegalOperandsException();
        }
    }

    @Override
    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        try {
            return new CraftrFloatValue(value / numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CraftrFloatValue(Float.MAX_VALUE);
        }
    }

    @Override
    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        try {
            return new CraftrFloatValue(value % numericOperand.getRawValue());
        } catch(ArithmeticException e) {
            return new CraftrFloatValue(Float.MAX_VALUE);
        }
    }

    @Override
    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number:int")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return new CraftrFloatValue((float) Math.pow(value, (int) numericOperand.getRawValue()));
    }

    @Override
    public CraftrValue increment() throws IllegalOperandsException {
        value++;
        return this;
    }

    @Override
    public CraftrValue decrement() throws IllegalOperandsException {
        value--;
        return this;
    }

    @Override
    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value > numericOperand.getRawValue();
    }

    @Override
    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value < numericOperand.getRawValue();
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value >= numericOperand.getRawValue();
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.value <= numericOperand.getRawValue();
    }

    @Override
    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) return false;
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return this.getRawValue() == numericOperand.getRawValue();
    }

    @Override
    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        this.value = (int) numericOperand.getRawValue();
        return this;
    }

    public static void init() {
        Evaluator.addEvaluator("NUMBER", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                int value;

                if(str.matches("\\d+(\\.\\d+)?")) {
                    value = Integer.valueOf(str);
                    return new CraftrIntegerValue(value);
                }

                return null;
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });
    }
}
