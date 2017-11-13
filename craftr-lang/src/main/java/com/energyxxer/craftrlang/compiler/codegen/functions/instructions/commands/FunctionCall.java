package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.codegen.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.Instruction;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull List<String> getLines() {
        return Collections.singletonList("function " + function.getName());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
