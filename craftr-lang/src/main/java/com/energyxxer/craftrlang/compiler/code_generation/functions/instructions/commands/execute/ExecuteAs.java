package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.semantic_analysis.commands.EntityReference;

public class ExecuteAs implements ExecuteSubCommand {
    private EntityReference entity;

    public ExecuteAs(EntityReference entity) {
        this.entity = entity;
    }

    @Override
    public Instruction getPreInstruction() {
        return entity.getInstruction();
    }

    @Override
    public String getSubCommand() {
        return "as " + entity.toSelector();
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
