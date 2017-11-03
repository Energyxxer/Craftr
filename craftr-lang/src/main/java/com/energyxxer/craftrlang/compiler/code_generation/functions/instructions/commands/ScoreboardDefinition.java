package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.Objective;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ScoreboardDefinition implements Instruction {
    private final Objective objective;

    public ScoreboardDefinition(Objective objective) {
        this.objective = objective;
    }

    @Override
    public Instruction getPreInstruction() {
        return null;
    }

    @Override
    public @NotNull List<String> getLines() {
        return Collections.singletonList("scoreboard objectives add " + objective.getName() + " " + objective.getType() + " " + objective.getDisplayName());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
