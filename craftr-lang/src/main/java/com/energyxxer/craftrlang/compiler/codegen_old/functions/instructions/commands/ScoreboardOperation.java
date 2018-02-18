package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands;

import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.CompoundInstruction;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.codegen.objectives.ResolvedObjectiveReference;
import com.energyxxer.util.Constant;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ScoreboardOperation implements Instruction {
    public static final Constant ASSIGN = new Constant("=", ScoreboardOperation.class);
    public static final Constant ADD = new Constant("+=", ScoreboardOperation.class);
    public static final Constant SUBTRACT = new Constant("-=", ScoreboardOperation.class);
    public static final Constant MULTIPLY = new Constant("*=", ScoreboardOperation.class);
    public static final Constant DIVIDE = new Constant("/=", ScoreboardOperation.class);
    public static final Constant MODULO = new Constant("%=", ScoreboardOperation.class);
    public static final Constant MIN = new Constant(">", ScoreboardOperation.class);
    public static final Constant MAX = new Constant("<", ScoreboardOperation.class);

    public ResolvedObjectiveReference a;
    public Constant operation;
    public ResolvedObjectiveReference b;

    public ScoreboardOperation(ResolvedObjectiveReference a, Constant operation, ResolvedObjectiveReference b) {
        this.a = a;
        this.setOperation(operation);
        this.b = b;
    }

    public ResolvedObjectiveReference getA() {
        return a;
    }

    public void setA(ResolvedObjectiveReference a) {
        this.a = a;
    }

    public Constant getOperation() {
        return operation;
    }

    public void setOperation(Constant operation) {
        if(!operation.isOfGroup(ScoreboardOperation.class)) throw new IllegalArgumentException("Illegal scoreboard operation subcommand '" + operation + "'");
        this.operation = operation;
    }

    public ResolvedObjectiveReference getB() {
        return b;
    }

    public void setB(ResolvedObjectiveReference b) {
        this.b = b;
    }

    @Override
    public Instruction getPreInstruction() {
        return new CompoundInstruction(a.getScoreHolderReference().getInstruction(),b.getScoreHolderReference().getInstruction());
    }

    @Override
    public @NotNull List<String> getLines() {
        return Collections.singletonList("scoreboard players operation " + a.getScoreHolderReference().getSelector() + " " + a.getObjective().getName() + " " + operation + " " + b.getScoreHolderReference().getSelector() + " " + b.getObjective().getName());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
