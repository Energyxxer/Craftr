package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;

public interface ExecuteSubCommand {
    Instruction getPreInstruction();
    String getSubCommand();
    Instruction getPostInstruction();
}
