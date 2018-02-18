package com.energyxxer.craftrlang.compiler.codegen.functions.instructions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompoundInstruction implements Instruction {
    ArrayList<Instruction> instructions = new ArrayList<>();

    public CompoundInstruction(Instruction... instructions) {
        this(Arrays.asList(instructions));
    }

    public CompoundInstruction(List<Instruction> instructions) {
        instructions.forEach(i -> {if(i != null) CompoundInstruction.this.instructions.add(i);});
    }

    public void addInstruction(Instruction instruction) {
        if(instruction != null) instructions.add(instruction);
    }

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public @NotNull List<String> getLines() {
        ArrayList<String> lines = new ArrayList<>();
        instructions.forEach(i -> {
            Instruction innerPreInstruction = i.getPreInstruction();
            List<String> innerPreLines = (innerPreInstruction != null) ? innerPreInstruction.getLines() : null;
            Instruction innerPostInstruction = i.getPostInstruction();
            List<String> innerPostLines = (innerPostInstruction != null) ? innerPostInstruction.getLines() : null;
            if(innerPreLines != null) lines.addAll(innerPreLines);
            lines.addAll(i.getLines());
            if(innerPostLines != null) lines.addAll(innerPostLines);
        });
        return lines;
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
