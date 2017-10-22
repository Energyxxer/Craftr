package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;

public interface EntityReference {
    String toSelector(MCFunction function);
}
