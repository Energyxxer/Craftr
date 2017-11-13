package com.energyxxer.craftrlang.compiler.codegen.players;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.codegen.DataPackBuilder;

public class ScoreHolderManager {
    private final DataPackBuilder dataPackBuilder;

    public final ScoreHolder RETURN;
    public final ScoreHolder CLONE;
    public final ScoreHolder MATH;

    public ScoreHolderManager(DataPackBuilder dataPackBuilder) {
        this.dataPackBuilder = dataPackBuilder;

        this.RETURN = new FakePlayer(this, "RETURN");
        this.CLONE = new FakePlayer(this, "CLONE");
        this.MATH = new FakePlayer(this, "MATH");
    }

    public Compiler getCompiler() {
        return dataPackBuilder.getCompiler();
    }

    public FakePlayer createFakePlayer(String name) {
        return new FakePlayer(this, name);
    }

    public DataPackBuilder getDataPackBuilder() {
        return dataPackBuilder;
    }
}
