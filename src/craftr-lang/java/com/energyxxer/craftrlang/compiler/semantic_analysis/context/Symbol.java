package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
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
    default @Nullable Package getPackage() {
        return null;
    }
    default @Nullable Unit getUnit() {
        return null;
    }
}
