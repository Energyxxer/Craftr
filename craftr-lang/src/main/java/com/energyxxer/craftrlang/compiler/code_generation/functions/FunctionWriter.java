package com.energyxxer.craftrlang.compiler.code_generation.functions;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public interface FunctionWriter {

    Value writeToFunction(MCFunction function);
}
