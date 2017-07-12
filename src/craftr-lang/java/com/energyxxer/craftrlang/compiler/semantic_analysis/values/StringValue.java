package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class StringValue extends Value {

    private String value = null;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public String toString() {
        return "StringValue{" +
                "value=" + value +
                ",explicit=" + this.explicit +
                '}';
    }
}
