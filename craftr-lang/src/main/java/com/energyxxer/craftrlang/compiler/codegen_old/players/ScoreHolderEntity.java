package com.energyxxer.craftrlang.compiler.codegen.players;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

public class ScoreHolderEntity extends ScoreHolder {

    private ObjectInstance instance;

    public ScoreHolderEntity(ScoreHolderManager scoreHolderManager, ObjectInstance instance) {
        super(scoreHolderManager);
        this.instance = instance;
    }

    @Override
    ScoreHolderReference createReference() {
        return new ScoreHolderReference(this, "<instance " + instance + ">");
    }
}
