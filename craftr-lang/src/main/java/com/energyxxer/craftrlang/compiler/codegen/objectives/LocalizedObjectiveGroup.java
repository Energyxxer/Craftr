package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.craftrlang.compiler.CraftrCommandModule;

import java.util.ArrayList;

public class LocalizedObjectiveGroup {
    private final LocalizedObjectiveManager parent;
    private final String name;
    private final ArrayList<LocalizedObjective> localizedObjectives;

    public LocalizedObjectiveGroup(LocalizedObjectiveManager parent, String name) {
        this.parent = parent;
        this.name = name;
        this.localizedObjectives = new ArrayList<>();
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
        LocalizedObjective localizedObjective = new LocalizedObjective(this, getModule().getObjectiveManager().get(name + slot), slot);
        replace(slot, localizedObjective);
        return localizedObjective;
    }

    public CraftrCommandModule getModule() {
        return parent.getModule();
    }

    void release(LocalizedObjective localizedObjective) {
        //Don't remove, just set to null
        localizedObjectives.set(localizedObjective.getSlot(), null);
    }

    @Override
    public String toString() {
        return "LocalizedObjectiveGroup:" + name;
    }
}
