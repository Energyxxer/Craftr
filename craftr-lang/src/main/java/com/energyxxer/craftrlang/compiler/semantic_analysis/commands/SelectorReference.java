package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;

public class SelectorReference implements EntityReference {
    private String selector;

    public SelectorReference() {
        this("@s");
    }

    public SelectorReference(String selector) {
        this.selector = selector;
    }

    @Override
    public String toSelector(MCFunction function) {
        return selector;
    }

    @Override
    public String toString() {
        return selector;
    }
}
