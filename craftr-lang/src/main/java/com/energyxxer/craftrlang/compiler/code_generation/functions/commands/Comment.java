package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

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
    public List<String> getLines() {
        return Collections.singletonList(content);
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
