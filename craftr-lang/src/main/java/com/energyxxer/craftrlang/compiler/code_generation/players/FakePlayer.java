package com.energyxxer.craftrlang.compiler.code_generation.players;

public class FakePlayer extends Player {

    private String name;
    private PlayerReference reference;

    public FakePlayer(PlayerManager playerManager, String name) {
        super(playerManager);
        this.name = playerManager.getCompiler().getPrefix() + "_" + name;
        this.reference = new PlayerReference(this, name);
    }

    @Override
    PlayerReference createReference() {
        return new PlayerReference(this, name);
    }
}
