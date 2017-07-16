package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class BooleanValue extends Value {

    private boolean value = false;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
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
        return "BooleanValue{" +
                "value=" + value +
                ",explicit=" + this.explicit +
                '}';
    }
}
