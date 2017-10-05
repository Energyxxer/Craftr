package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ActualParameter {
    private String name = null;
    private Value value;

    public ActualParameter(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    public ActualParameter(Value value) {
        this.value = value;
    }
}
