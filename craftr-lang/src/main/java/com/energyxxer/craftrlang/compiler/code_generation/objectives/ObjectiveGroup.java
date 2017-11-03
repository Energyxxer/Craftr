package com.energyxxer.craftrlang.compiler.code_generation.objectives;

import com.energyxxer.craftrlang.compiler.code_generation.players.Player;

import java.util.ArrayList;

public class ObjectiveGroup {
    private Player parent;

    private String prefix;
    private String displayNameBase;

    private ArrayList<UnresolvedObjectiveReference> objectives = new ArrayList<>();

    public ObjectiveGroup(Player parent, String prefix, String displayNameBase) {
        this.parent = parent;
        this.prefix = prefix;
        this.displayNameBase = displayNameBase;
    }

    public UnresolvedObjectiveReference get() {
        for(UnresolvedObjectiveReference objective : objectives) {
            if(!objective.isInUse()) {
                return objective;
            }
        }
        return createNew();
    }

    private UnresolvedObjectiveReference createNew() {
        String name = parent.getDataPackBuilder().getCompiler().getPrefix() + "_" + prefix + objectives.size();
        String displayName = displayNameBase + " " + objectives.size();
        Objective objective = parent.getDataPackBuilder().getObjectiveManager().createObjective(name, displayName);

        UnresolvedObjectiveReference local = new UnresolvedObjectiveReference(objective, parent);
        this.objectives.add(local);
        return local;
    }
}
