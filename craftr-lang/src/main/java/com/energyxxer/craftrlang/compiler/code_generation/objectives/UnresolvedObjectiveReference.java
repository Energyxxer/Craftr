package com.energyxxer.craftrlang.compiler.code_generation.objectives;

import com.energyxxer.craftrlang.compiler.code_generation.players.Player;
import com.energyxxer.craftrlang.compiler.code_generation.players.PlayerReference;

public class UnresolvedObjectiveReference {
    private Objective objective;
    private Player player;
    private boolean inUse = false;

    public UnresolvedObjectiveReference(Objective objective, Player player) {
        this.objective = objective;
        this.player = player;
    }

    public Objective getObjective() {
        return objective;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public ResolvedObjectiveReference resolveTo(PlayerReference playerReference) {
        return new ResolvedObjectiveReference(this, playerReference);
    }

    @Override
    public String toString() {
        return (inUse ? "*" : "") + objective + " [" + player + "]";
    }
}
