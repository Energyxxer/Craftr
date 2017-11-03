package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

public class EntityPlayer extends Player {

    private ObjectInstance instance;

    public EntityPlayer(PlayerManager playerManager, ObjectInstance instance) {
        super(playerManager);
        this.instance = instance;
    }

    @Override
    PlayerReference createReference() {
        return new PlayerReference(this, "<instance " + instance + ">");
    }
}
