package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;
import com.energyxxer.util.Constant;

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

    public ObjectivePointer a;
    public Constant operation;
    public ObjectivePointer b;

    public ScoreboardOperation(ObjectivePointer a, Constant operation, ObjectivePointer b) {
        this.a = a;
        this.setOperation(operation);
        this.b = b;
    }

    public ObjectivePointer getA() {
        return a;
    }

    public void setA(ObjectivePointer a) {
        this.a = a;
    }

    public Constant getOperation() {
        return operation;
    }

    public void setOperation(Constant operation) {
        if(!operation.isOfGroup(ScoreboardOperation.class)) throw new IllegalArgumentException("Illegal scoreboard operation subcommand '" + operation + "'");
        this.operation = operation;
    }

    public ObjectivePointer getB() {
        return b;
    }

    public void setB(ObjectivePointer b) {
        this.b = b;
    }

    @Override
    public Instruction getPreInstruction() {
        return new CompoundInstruction(a.getEntity().getInstruction(),b.getEntity().getInstruction());
    }

    @Override
    public List<String> getLines() {
        return Collections.singletonList("scoreboard players operation " + a.getEntity().toSelector() + " " + a.getObjectiveName() + " " + operation + " " + b.getEntity().toSelector() + " " + b.getObjectiveName());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
