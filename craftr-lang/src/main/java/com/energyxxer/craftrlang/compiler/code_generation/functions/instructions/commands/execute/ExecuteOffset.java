package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.coordinates.CoordinateSet;

public class ExecuteOffset implements ExecuteSubCommand {

    public CoordinateSet offset;

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public String getSubCommand() {
        return "offset " + offset;
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }

    @Override
    public String toString() {
        return getSubCommand();
    }
}
