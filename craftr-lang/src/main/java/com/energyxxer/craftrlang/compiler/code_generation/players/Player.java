package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.code_generation.DataPackBuilder;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.ObjectiveGroup;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;

public abstract class Player {
    private final PlayerManager playerManager;

    public final ObjectiveGroup OPERATION;
    public final ObjectiveGroup PARAMETER;
    public final ObjectiveGroup GENERIC;
    public final ObjectiveGroup ID_OPERATION;

    public final UnresolvedObjectiveReference ID;

    public Player(PlayerManager playerManager) {
        this.playerManager = playerManager;

        this.OPERATION = new ObjectiveGroup(this, "op", "Operation");
        this.PARAMETER = new ObjectiveGroup(this, "p", "Parameter");
        this.GENERIC = new ObjectiveGroup(this, "g", "Generic");
        this.ID_OPERATION = new ObjectiveGroup(this, "id_op", "ID Operation");
        this.ID = new UnresolvedObjectiveReference(playerManager.getDataPackBuilder().getObjectiveManager().createObjective("id","Entity ID"), this);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DataPackBuilder getDataPackBuilder() {
        return playerManager.getDataPackBuilder();
    }

    public PlayerReference resolvePlayer(Player player) {
        if(this == player) return new PlayerReference(this, "@s");
        return player.createReference();
    }

    abstract PlayerReference createReference();
}
