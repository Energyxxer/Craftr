package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.codegen.players.ScoreHolderReference;

public class ExecuteAs implements ExecuteSubCommand {
    private ScoreHolderReference entity;

    public ExecuteAs(ScoreHolderReference entity) {
        this.entity = entity;
    }

    @Override
    public Instruction getPreInstruction() {
        return entity.getInstruction();
    }

    @Override
    public String getSubCommand() {
        return "as " + entity.getSelector();
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
