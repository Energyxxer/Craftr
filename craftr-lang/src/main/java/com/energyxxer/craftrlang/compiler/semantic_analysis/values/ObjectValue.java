package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class ObjectValue extends Value {

    private Unit unit;

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return unit.getFieldManager().getInstanceFieldTable();
    }

    @Override
    public MethodManager getMethodManager() {
        return unit.getMethodManager();
    }
}
