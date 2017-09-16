package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class IntegerValue extends NumericalValue {

    private int value = 0;

    public IntegerValue(int value, Context context) {
        super(context);
        this.value = value;
    }

    @Override
    public NumericalValue coerce(NumericalValue other) {
        if(other instanceof IntegerValue) return this;
        if(other instanceof FloatValue) return new FloatValue(this.value, context);
        return null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        if(operand instanceof NumericalValue) {
            int weightDiff = this.getWeight() - ((NumericalValue) operand).getWeight();
            if(weightDiff > 0) return operation(operator, ((NumericalValue) operand).coerce(this), pattern);
            else if(weightDiff < 0) return this.coerce((NumericalValue) operand).operation(operator, operand, pattern);
            else {
                //We can be certain that if this code is running, then both operands are IntegerValues

                IntegerValue intOperand = (IntegerValue) operand;

                switch(operator) {
                    case ADD: return new IntegerValue(this.value + intOperand.value, context);
                    case SUBTRACT: return new IntegerValue(this.value - intOperand.value, context);
                    case MULTIPLY: return new IntegerValue(this.value * intOperand.value, context);
                    case DIVIDE: return new IntegerValue(this.value / intOperand.value, context);//Should probably add a case for division by zero
                    case MODULO: return new IntegerValue(this.value % intOperand.value, context); //Should probably add a case for division by zero
                    case EQUAL: return new BooleanValue(this.value == intOperand.value, context);
                    case LESS_THAN: return new BooleanValue(this.value < intOperand.value, context);
                    case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value <= intOperand.value, context);
                    case GREATER_THAN: return new BooleanValue(this.value > intOperand.value, context);
                    case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value >= intOperand.value, context);
                }
                return null;
            }
        } else if(operand instanceof StringValue && operator == Operator.ADD) {
            return new StringValue(String.valueOf(this.value)+((StringValue)operand).getValue(), this.context);
        }
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public DataType getDataType() {
        return DataType.INT;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodManager getMethodManager() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
