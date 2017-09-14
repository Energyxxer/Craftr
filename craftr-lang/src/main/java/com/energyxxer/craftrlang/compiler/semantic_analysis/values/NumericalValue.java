package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

public class NumericalValue<T extends Number> extends Value {

    protected T value;

    public NumericalValue(T value, Context context) {
        super(context);
        this.value = value;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        switch(operator) {
            case ADD: return new NumericalValue<>(this.value, context);
            case SUBTRACT: return new NumericalValue<>(-(this.value).doubleValue(), context);
            //case INCREMENT: return new NumericalValue<>(this.value + 1, context);
            //case DECREMENT: return new NumericalValue<>(this.value - 1, context);
            //(Increment, decrement and shorthand assignment operations are done on the field/variable side
        }
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        if(operand instanceof NumericalValue<?>) {

            double opResult;

            switch(operator) {
                case ADD: opResult = this.value.doubleValue() + ((NumericalValue) operand).value.doubleValue(); break;
                case SUBTRACT: opResult = this.value.doubleValue() - ((NumericalValue) operand).value.doubleValue(); break;
                case MULTIPLY: opResult = this.value.doubleValue() * ((NumericalValue) operand).value.doubleValue(); break;
                case DIVIDE: opResult = this.value.doubleValue() / ((NumericalValue) operand).value.doubleValue(); break;
                case MODULO: opResult = this.value.doubleValue() % ((NumericalValue) operand).value.doubleValue(); break;
                case LESS_THAN: return new BooleanValue(this.value.doubleValue() < ((NumericalValue) operand).value.doubleValue(), context);
                case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value.doubleValue() <= ((NumericalValue) operand).value.doubleValue(), context);
                case GREATER_THAN: return new BooleanValue(this.value.doubleValue() > ((NumericalValue) operand).value.doubleValue(), context);
                case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value.doubleValue() >= ((NumericalValue) operand).value.doubleValue(), context);
                default: return null;
            }

            Number result;
            if(this.value instanceof Float || ((NumericalValue) operand).value instanceof Float) result = (float) opResult;
            else result = (int) opResult;

            return new NumericalValue<>(result, context);
        }
        return null;
    }

    @Override
    public DataType getDataType() {
        if(value instanceof Integer) return DataType.INT;
        else if(value instanceof Float) return DataType.FLOAT;
        else {
            System.out.println("Unknown numeric data type: " + value.getClass().getSimpleName());
            return DataType.INT;
        }
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodManager getMethodManager() {
        return null;
    }
}
