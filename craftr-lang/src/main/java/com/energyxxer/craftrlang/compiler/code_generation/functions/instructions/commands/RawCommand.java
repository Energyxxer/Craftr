package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RawCommand implements Instruction {
    private Instruction preInstruction = null;
    private String command;
    private Instruction postInstruction = null;

    public RawCommand(String command) {
        this.command = command;
    }

    @Override
    public Instruction getPreInstruction() {
        return preInstruction;
    }

    public void setPreInstruction(Instruction preInstruction) {
        this.preInstruction = preInstruction;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public Instruction getPostInstruction() {
        return postInstruction;
    }

    public void setPostInstruction(Instruction postInstruction) {
        this.postInstruction = postInstruction;
    }

    @Override
    public @NotNull List<String> getLines() {
        return Collections.singletonList(command);
    }
}
