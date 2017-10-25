package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public class SelectorReference extends EntityReference {
    private String selector;

    public SelectorReference(Context context) {
        this("@s", context);
    }

    public SelectorReference(String selector, Context context) {
        super(context);
        this.selector = selector;
    }

    @Override
    public Instruction getInstruction() {
        return null;
    }

    @Override
    public String toSelector() {
        return selector;
    }

    @Override
    public String toString() {
        return selector;
    }
}
