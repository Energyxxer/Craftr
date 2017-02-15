package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 12/20/2016.
 */
public class CraftrFloatValue extends CraftrNumericValue {

    private float value;

    public CraftrFloatValue(float value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "float";
    }

    @Override
    public String getInternalType() {
        return "number:float";
    }

    @Override
    public int getWeight() {
        return 1;
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
        return new CraftrFloatValue(value + numericOperand.getRawValue());
    }

    @Override
    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return new CraftrFloatValue(value - numericOperand.getRawValue());
    }

    @Override
    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {

        String String = "a";
        System.out.println(String.toUpperCase());

        if(!operand.getInternalType().contains("number")) throw new IllegalOperandsException();
        CraftrNumericValue numericOperand = (CraftrNumericValue) operand;
        return new CraftrFloatValue(value * numericOperand.getRawValue());
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
        this.value = numericOperand.getRawValue();
        return this;
    }

    public static void init() {
        Evaluator.addEvaluator("NUMBER", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                float value;

                if(str.matches("\\d+(\\.\\d+)?(f)?")) {
                    if(str.endsWith("f")) {
                        str = str.substring(0,str.length()-1);
                    }
                    value = Float.valueOf(str);
                    return new CraftrFloatValue(value);
                }

                return null;
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });
    }
}