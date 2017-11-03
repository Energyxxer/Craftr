package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.code_generation.DataPackBuilder;

public class PlayerManager {
    private final DataPackBuilder dataPackBuilder;

    public final Player RETURN;
    public final Player CLONE;
    public final Player MATH;

    public PlayerManager(DataPackBuilder dataPackBuilder) {
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
