package com.energyxxer.craftrlang.compiler.code_generation.objectives;

public class Objective {
    private final String name;
    private final String displayName;
    private String type = "dummy";

    public Objective(String name, String displayName, String type) {
        this.name = name;
        this.displayName = displayName;
        if(type != null) this.type = type;
    }

    public Objective(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + displayName + ")";
    }
}
