package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.code_generation.DataPackBuilder;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.ObjectiveGroup;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;

/**
 * A score holder, not a player entity
 * */
public abstract class ScoreHolder {
    private final ScoreHolderManager scoreHolderManager;

    public final ObjectiveGroup OPERATION;
    public final ObjectiveGroup PARAMETER;
    public final ObjectiveGroup GENERIC;
    public final ObjectiveGroup ID_OPERATION;

    public final UnresolvedObjectiveReference ID;

    public ScoreHolder(ScoreHolderManager scoreHolderManager) {
        this.scoreHolderManager = scoreHolderManager;

        this.OPERATION = new ObjectiveGroup(this, "op", "Operation");
        this.PARAMETER = new ObjectiveGroup(this, "p", "Parameter");
        this.GENERIC = new ObjectiveGroup(this, "g", "Generic");
        this.ID_OPERATION = new ObjectiveGroup(this, "id_op", "ID Operation");
        this.ID = new UnresolvedObjectiveReference(scoreHolderManager.getDataPackBuilder().getObjectiveManager().createObjective("id","Entity ID"), this);
    }

    public ScoreHolderManager getScoreHolderManager() {
        return scoreHolderManager;
    }

    public DataPackBuilder getDataPackBuilder() {
        return scoreHolderManager.getDataPackBuilder();
    }

    public ScoreHolderReference resolvePlayer(ScoreHolder scoreHolder) {
        if(this == scoreHolder) return new ScoreHolderReference(this, "@s");
        return scoreHolder.createReference();
    }

    abstract ScoreHolderReference createReference();
}
