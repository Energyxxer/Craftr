package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public abstract class Value {

    protected boolean explicit = true;

    public boolean isExplicit() {
        return explicit;
    }

    protected void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public abstract DataType getDataType();
    public abstract SymbolTable getSubSymbolTable();
}
