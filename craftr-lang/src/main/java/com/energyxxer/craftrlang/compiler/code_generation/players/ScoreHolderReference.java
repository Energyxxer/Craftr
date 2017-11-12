package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;

public class ScoreHolderReference {
    private ScoreHolder scoreHolder;
    private String selector;
    private Instruction instruction;

    public ScoreHolderReference(ScoreHolder scoreHolder, String selector) {
        this(scoreHolder, selector, null);
    }

    public ScoreHolderReference(ScoreHolder scoreHolder, String selector, Instruction instruction) {
        this.scoreHolder = scoreHolder;
        this.selector = selector;
        this.instruction = instruction;
    }

    public ScoreHolder getScoreHolder() {
        return scoreHolder;
    }

    public String getSelector() {
        return selector;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return "ScoreHolderReference{" +
                "scoreHolder=" + scoreHolder +
                ", selector='" + selector + '\'' +
                ", instruction=" + instruction +
                '}';
    }
}
