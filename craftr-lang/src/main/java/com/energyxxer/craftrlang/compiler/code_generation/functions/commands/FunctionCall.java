package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;

import java.util.Collections;
import java.util.List;

public class FunctionCall implements Instruction {
    private MCFunction function;

    public FunctionCall(MCFunction function) {
        this.function = function;
    }

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public List<String> getLines() {
        return Collections.singletonList("function " + function.getName());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
