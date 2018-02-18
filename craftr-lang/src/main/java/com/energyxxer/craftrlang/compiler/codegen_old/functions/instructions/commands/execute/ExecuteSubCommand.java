package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.Instruction;

public interface ExecuteSubCommand {
    Instruction getPreInstruction();
    String getSubCommand();
    Instruction getPostInstruction();
}
