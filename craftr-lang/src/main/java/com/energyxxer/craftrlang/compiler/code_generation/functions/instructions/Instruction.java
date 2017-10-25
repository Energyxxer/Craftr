package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Instruction {
    Instruction getPreInstruction();
    @NotNull List<String> getLines();
    Instruction getPostInstruction();
}
