package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.players.PlayerReference;

public class ExecuteAt implements ExecuteSubCommand {
    private PlayerReference entity;

    public ExecuteAt(PlayerReference entity) {
        this.entity = entity;
    }

    @Override
    public Instruction getPreInstruction() {
        return entity.getInstruction();
    }

    @Override
    public String getSubCommand() {
        return "at " + entity.getSelector();
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
