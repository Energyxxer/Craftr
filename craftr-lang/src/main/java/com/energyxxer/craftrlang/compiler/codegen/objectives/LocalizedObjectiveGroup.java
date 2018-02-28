package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.score.Objective;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;

import java.util.ArrayList;

public class LocalizedObjectiveGroup {
    private final LocalizedObjectiveManager parent;
    private final String name;
    private final ArrayList<LocalizedObjective> localizedObjectives;
    private final boolean field;

    public LocalizedObjectiveGroup(LocalizedObjectiveManager parent, String name) {
        this(parent, name, false);
    }

    public LocalizedObjectiveGroup(LocalizedObjectiveManager parent, String name, boolean field) {
        this.parent = parent;
        this.name = name;
        this.localizedObjectives = new ArrayList<>();
        this.field = field;
    }

    private int getVacantSlot() {
        int i = 0;
        for (; i < localizedObjectives.size(); i++) {
            if (!localizedObjectives.get(i).isCaptured()) return i;
        }
        return i;
    }

    private void replace(int slot, LocalizedObjective localizedObjective) {
        if(slot < localizedObjectives.size()) {
            localizedObjectives.get(slot).dispose();
            localizedObjectives.set(slot, localizedObjective);
        } else {
            localizedObjectives.add(localizedObjective);
        }
    }

    public LocalizedObjective create() {
        int slot = getVacantSlot();

        Objective objective;
        if(getModule().getObjectiveManager().contains(name + slot)) objective = getModule().getObjectiveManager().get(name + slot);
        else objective = getModule().getObjectiveManager().create(name + slot, field);

        LocalizedObjective localizedObjective = new LocalizedObjective(this, objective, slot);
        replace(slot, localizedObjective);
        return localizedObjective;
    }

    public CraftrCommandModule getModule() {
        return parent.getModule();
    }

    @Override
    public String toString() {
        return "LocalizedObjectiveGroup:" + name;
    }
}
