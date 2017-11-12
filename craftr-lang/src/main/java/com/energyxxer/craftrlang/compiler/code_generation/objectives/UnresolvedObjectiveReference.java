package com.energyxxer.craftrlang.compiler.code_generation.objectives;

import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolder;
import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolderReference;

public class UnresolvedObjectiveReference {
    private Objective objective;
    private ScoreHolder scoreHolder;
    private boolean inUse = false;

    public UnresolvedObjectiveReference(Objective objective, ScoreHolder scoreHolder) {
        this.objective = objective;
        this.scoreHolder = scoreHolder;
    }

    public Objective getObjective() {
        return objective;
    }

    public ScoreHolder getScoreHolder() {
        return scoreHolder;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public ResolvedObjectiveReference resolveTo(ScoreHolderReference scoreHolderReference) {
        return new ResolvedObjectiveReference(this, scoreHolderReference);
    }

    @Override
    public String toString() {
        return (inUse ? "*" : "") + objective + " [" + scoreHolder + "]";
    }
}
