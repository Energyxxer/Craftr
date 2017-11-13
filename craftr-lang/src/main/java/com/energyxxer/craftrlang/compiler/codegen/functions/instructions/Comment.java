package com.energyxxer.craftrlang.compiler.codegen.functions.instructions;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Comment implements Instruction {
    private String content;

    public Comment(String content) {
        this.content = "# " + content;
    }

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public @NotNull List<String> getLines() {
        return Collections.singletonList(content);
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
