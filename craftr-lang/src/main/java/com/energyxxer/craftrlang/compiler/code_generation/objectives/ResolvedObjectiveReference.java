package com.energyxxer.craftrlang.compiler.code_generation.objectives;

import com.energyxxer.craftrlang.compiler.code_generation.players.Player;
import com.energyxxer.craftrlang.compiler.code_generation.players.PlayerReference;

public class ResolvedObjectiveReference {
    private final UnresolvedObjectiveReference unresolvedObjectiveReference;
    private final PlayerReference playerReference;

    ResolvedObjectiveReference(UnresolvedObjectiveReference unresolvedObjectiveReference, PlayerReference playerReference) {
        if(unresolvedObjectiveReference.getPlayer() != playerReference.getPlayer()) throw new IllegalArgumentException("At ResolvedObjectiveReference constructor: unresolvedObjectiveReference's player (" + unresolvedObjectiveReference.getPlayer() + ") does not correspond to playerReference's (" + playerReference.getPlayer() + ")");

        this.unresolvedObjectiveReference = unresolvedObjectiveReference;
        this.playerReference = playerReference;
    }

    public void setInUse(boolean inUse) {
        unresolvedObjectiveReference.setInUse(inUse);
    }

    public boolean isInUse() {
        return unresolvedObjectiveReference.isInUse();
    }

    public Player getPlayer() {
        return playerReference.getPlayer();
    }

    public PlayerReference getPlayerReference() {
        return playerReference;
    }

    public Objective getObjective() {
        return unresolvedObjectiveReference.getObjective();
    }

    public UnresolvedObjectiveReference getUnresolvedObjectiveReference() {
        return unresolvedObjectiveReference;
    }
}
