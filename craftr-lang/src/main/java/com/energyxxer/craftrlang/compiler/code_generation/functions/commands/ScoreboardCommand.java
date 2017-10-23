package com.energyxxer.craftrlang.compiler.code_generation.functions.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.Score;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;
import com.energyxxer.util.Constant;

import java.util.Collections;
import java.util.List;

public class ScoreboardCommand implements Instruction {
    public static final Constant SET = new Constant("set", ScoreboardCommand.class);
    public static final Constant ADD = new Constant("add", ScoreboardCommand.class);
    public static final Constant REMOVE = new Constant("remove", ScoreboardCommand.class);

    public ObjectivePointer pointer;
    public Constant operation = SET;
    public Score value;

    public ScoreboardCommand(ObjectivePointer pointer, Constant operation, Score value) {
        this.pointer = pointer;
        this.setOperation(operation);
        this.value = value;
    }

    public ObjectivePointer getPointer() {
        return pointer;
    }

    public void setPointer(ObjectivePointer pointer) {
        this.pointer = pointer;
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
        return pointer.getEntity().getInstruction();
    }

    @Override
    public List<String> getLines() {
        return Collections.singletonList("scoreboard players " + operation + " " + pointer.getEntity().toSelector() + " " + pointer.getObjectiveName() + " " + value.getScoreboardValue());
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }
}
