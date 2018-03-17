package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.score.Objective;

public class LocalizedObjective {
    private final LocalizedObjectiveGroup group;
    private final Objective objective;
    private final int slot;

    private boolean captured;
    private boolean disposed = false;

    LocalizedObjective(LocalizedObjectiveGroup group, Objective objective, int slot) {
        this.group = group;
        this.objective = objective;
        this.slot = slot;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void capture() {
        captured = true;
    }

    public void release() {
        captured = false;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void dispose() {
        release();
        this.disposed = true;
    }

    public LocalizedObjectiveGroup getGroup() {
        return group;
    }

    public Objective getObjective() {
        if(disposed) throw new IllegalStateException("Attempted to retrieve a disposed localized objective");
        return objective;
    }

    int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "LocalizedObjective:" + objective + "(captured:" + captured + ")";
    }
}
