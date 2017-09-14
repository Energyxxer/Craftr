package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class IntegerValue extends NumericalValue<Integer> {

    public IntegerValue(int value, Context context) {
        super(value, context);
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
        return "IntegerValue{" +
                "value=" + value +
                ",explicit=" + this.explicit +
                '}';
    }
}
