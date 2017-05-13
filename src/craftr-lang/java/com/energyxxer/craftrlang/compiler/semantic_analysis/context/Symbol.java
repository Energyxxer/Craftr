package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import org.jetbrains.annotations.Nullable;

/**
 * Created by User on 3/14/2017.
 */
public interface Symbol {
    String getName();
    default @Nullable SymbolTable getSubSymbolTable() {
        return null;
    }
    SymbolVisibility getVisibility();
}
