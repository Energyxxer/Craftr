package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.Score;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.ResolvedObjectiveReference;
import com.energyxxer.util.Constant;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ScoreboardCommand implements Instruction {
    public static final Constant SET = new Constant("set", ScoreboardCommand.class);
    public static final Constant ADD = new Constant("add", ScoreboardCommand.class);
    public static final Constant REMOVE = new Constant("remove", ScoreboardCommand.class);

    public ResolvedObjectiveReference reference;
    public Constant operation = SET;
    public Score value;

    public ScoreboardCommand(ResolvedObjectiveReference reference, Constant operation, Score value) {
        this.reference = reference;
        this.setOperation(operation);
        this.value = value;
    }

    public ResolvedObjectiveReference getReference() {
        return reference;
    }

    public void setReference(ResolvedObjectiveReference reference) {
        this.reference = reference;
    }

    public Constant getOperation() {
        return operation;
    }

    public void setOperation(Constant operation) {
        if(!operation.isOfGroup(ScoreboardCommand.class)) throw new IllegalArgumentException("Illegal scoreboard players subcommand '" + operation + "'");
        this.operation = operation;
    }

    public Score getValue() {
        return value;
    }

    public void setValue(Score value) {
        this.value = value;
    }

    @Override
    public Instruction getPreInstruction() {
        return reference.getScoreHolderReference().getInstruction();
    }

    @Override
    public @NotNull List<String> getLines() {
        return Collections.singletonList("scoreboard players " + operation + " " + reference.getScoreHolderReference().getSelector() + " " + reference.getObjective().getName() + " " + value.getScoreboardValue());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
