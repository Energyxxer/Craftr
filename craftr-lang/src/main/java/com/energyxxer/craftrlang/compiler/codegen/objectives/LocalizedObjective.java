package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.score.Objective;

public class LocalizedObjective {
    private final LocalizedObjectiveGroup group;
    private final Objective objective;
    private final int slot;

    private LocalizedObjectiveState state = LocalizedObjectiveState.UNCLAIMED;

    LocalizedObjective(LocalizedObjectiveGroup group, Objective objective, int slot) {
        this.group = group;
        this.objective = objective;
        this.slot = slot;
    }

    public void claim() {
        if(state == LocalizedObjectiveState.UNCLAIMED) state = LocalizedObjectiveState.CLAIMED;
        else throw new IllegalStateException("Cannot claim an already-claimed or disposed objective");
    }

    //Difference between release() and dispose() is in how subsequent uses of this object will play out;
    //If released, contexts which still have a reference to this object will still be able to get its objective.
    //If disposed, contexts which still have this object will throw an exception when the objective is attempted to get.

    public void release() {
        if(state == LocalizedObjectiveState.CLAIMED) state = LocalizedObjectiveState.UNCLAIMED;
        else throw new IllegalStateException("Cannot release an unclaimed or disposed objective");
    }

    public void dispose() {
        state = LocalizedObjectiveState.DISPOSED;
    }

    public LocalizedObjectiveGroup getGroup() {
        return group;
    }

    public LocalizedObjectiveState getState() {
        return state;
    }

    public Objective getObjective() {
        if(state == LocalizedObjectiveState.DISPOSED) throw new IllegalStateException("Attempted to retrieve a disposed localized objective");
        return objective;
    }

    int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "LocalizedObjective:" + objective + "(state:" + state + ")";
    }
}
