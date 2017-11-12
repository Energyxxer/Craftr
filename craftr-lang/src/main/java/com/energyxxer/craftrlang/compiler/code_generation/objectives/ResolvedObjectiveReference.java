package com.energyxxer.craftrlang.compiler.code_generation.objectives;

import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolder;
import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolderReference;

public class ResolvedObjectiveReference {
    private final UnresolvedObjectiveReference unresolvedObjectiveReference;
    private final ScoreHolderReference scoreHolderReference;

    ResolvedObjectiveReference(UnresolvedObjectiveReference unresolvedObjectiveReference, ScoreHolderReference scoreHolderReference) {
        if(unresolvedObjectiveReference.getScoreHolder() != scoreHolderReference.getScoreHolder()) throw new IllegalArgumentException("At ResolvedObjectiveReference constructor: unresolvedObjectiveReference's player (" + unresolvedObjectiveReference.getScoreHolder() + ") does not correspond to scoreHolderReference's (" + scoreHolderReference.getScoreHolder() + ")");

        this.unresolvedObjectiveReference = unresolvedObjectiveReference;
        this.scoreHolderReference = scoreHolderReference;
    }

    public void setInUse(boolean inUse) {
        unresolvedObjectiveReference.setInUse(inUse);
    }

    public boolean isInUse() {
        return unresolvedObjectiveReference.isInUse();
    }

    public ScoreHolder getPlayer() {
        return scoreHolderReference.getScoreHolder();
    }

    public ScoreHolderReference getScoreHolderReference() {
        return scoreHolderReference;
    }

    public Objective getObjective() {
        return unresolvedObjectiveReference.getObjective();
    }

    public UnresolvedObjectiveReference getUnresolvedObjectiveReference() {
        return unresolvedObjectiveReference;
    }
}
