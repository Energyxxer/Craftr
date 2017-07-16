package com.energyxxer.craftrlang.compiler.semantic_analysis;

/**
 * Created by Energyxxer on 07/14/2017.
 */
public enum UnitType {
    ENTITY("Entity", "Entities"), ITEM("Item", "Items"), FEATURE("Feature", "Features"), CLASS("Class", "Classes"), ENUM("Enum", "Enums");

    private final String name;
    private final String plural;

    UnitType(String name, String plural) {
        this.name = name;
        this.plural = plural;
    }

    public String getName() {
        return name;
    }

    public String getPlural() {
        return plural;
    }
}
