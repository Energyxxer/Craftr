package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

import com.energyxxer.craftrlang.compiler.semantic_analysis.commands.EntityReference;

import java.util.List;

public class ExecuteAsCommand implements Instruction {

    private EntityReference entity;
    private Instruction command;

    public ExecuteAsCommand(EntityReference entity, Instruction command) {
        this.entity = entity;
        this.command = command;
    }

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public List<String> getLines() {
        String heading = "execute as " + entity.toSelector() + " then ";
        List<String> innerLines = command.getLines();
        for(int i = 0; i < innerLines.size(); i++) {
            innerLines.set(i, heading + innerLines.get(i));
        }
        return innerLines;
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
