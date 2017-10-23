package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

import java.util.List;

public interface Instruction {
    Instruction getPreInstruction();
    List<String> getLines();
    Instruction getPostInstruction();
}
