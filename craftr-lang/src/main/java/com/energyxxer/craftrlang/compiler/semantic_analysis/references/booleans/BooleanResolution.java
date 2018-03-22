package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.commands.execute.ExecuteModifier;

import java.util.Arrays;
import java.util.Collection;

public class BooleanResolution {
    public enum State {
        TRUE, FALSE, IMPLICIT;
    }

    private State state;
    private Collection<ExecuteModifier> modifiers;

    public BooleanResolution(boolean state) {
        this.state = state ? State.TRUE : State.FALSE;
    }

    public BooleanResolution(ExecuteModifier... modifiers) {
        this.modifiers = Arrays.asList(modifiers);
    }

    public State getState() {
        return state;
    }

    public Collection<ExecuteModifier> getModifiers() {
        return modifiers;
    }
}
