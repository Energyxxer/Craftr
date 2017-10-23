package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.Instruction;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public abstract class EntityReference {
    protected final Context context;

    public EntityReference(Context context) {
        this.context = context;
    }

    public abstract Instruction getInstruction();
    public abstract String toSelector();
}
