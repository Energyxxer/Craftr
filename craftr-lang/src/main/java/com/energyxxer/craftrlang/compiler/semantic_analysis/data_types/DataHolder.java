package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

public interface DataHolder {

    SymbolTable getSubSymbolTable();
    MethodLog getMethodLog();

    ObjectInstance asObjectInstance();
}
