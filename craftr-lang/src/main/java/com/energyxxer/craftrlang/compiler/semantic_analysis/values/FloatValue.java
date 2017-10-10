package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class FloatValue extends NumericalValue {

    private float value = 0;

    public FloatValue(float value, Context context) {
        super(context);
        this.value = value;
    }

    @Override
    public NumericalValue coerce(NumericalValue value) {
        return this;
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
                //We can be certain that if this code is running, then both operands are FloatValues

                FloatValue floatOperand = (FloatValue) operand;

                switch(operator) {
                    case ADD: return new FloatValue(this.value + floatOperand.value, context);
                    case SUBTRACT: return new FloatValue(this.value - floatOperand.value, context);
                    case MULTIPLY: return new FloatValue(this.value * floatOperand.value, context);
                    case DIVIDE: return new FloatValue(this.value / floatOperand.value, context);//Should probably add a case for division by zero
                    case MODULO: return new FloatValue(this.value % floatOperand.value, context); //Should probably add a case for division by zero
                    case EQUAL: return new BooleanValue(this.value == floatOperand.value, context);
                    case LESS_THAN: return new BooleanValue(this.value < floatOperand.value, context);
                    case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value <= floatOperand.value, context);
                    case GREATER_THAN: return new BooleanValue(this.value > floatOperand.value, context);
                    case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value >= floatOperand.value, context);
                }
                return null;
            }
        } else if(operand instanceof StringValue && operator == Operator.ADD) {
            return new StringValue(String.valueOf(this.value)+((StringValue)operand).getRawValue(), this.context);
        }
        return null;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public DataType getDataType() {
        return DataType.FLOAT;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodLog getMethodLog() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public Float getRawValue() {
        return this.value;
    }
}
