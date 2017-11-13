package com.energyxxer.craftrlang.compiler.codegen.players;

public class FakePlayer extends ScoreHolder {

    private String name;
    private ScoreHolderReference reference;

    public FakePlayer(ScoreHolderManager scoreHolderManager, String name) {
        super(scoreHolderManager);
        this.name = scoreHolderManager.getCompiler().getPrefix() + "_" + name;
        this.reference = new ScoreHolderReference(this, name);
    }

    @Override
    ScoreHolderReference createReference() {
        return new ScoreHolderReference(this, name);
    }
}
