package com.energyxxer.craftrlang.compiler.codegen;

import com.energyxxer.commodore.functions.Function;

public interface CommandWriter {
    //TODO: Eventually, when all old occurrences of writeToFunction are gone, rename this
    void writeCommands(Function function);
}
