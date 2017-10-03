package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Energyxxer on 07/14/2017.
 */
public enum UnitType {
    ENTITY("Entity", "Entities"), ITEM("Item", "Items"), FEATURE("Feature", "Features"), CLASS("Class", "Classes"), ENUM("Enum", "Enums"), WORLD("World", "Worlds", new CraftrUtil.Modifier[] {CraftrUtil.Modifier.STATIC});

    private final String name;
    private final String plural;
    private final List<CraftrUtil.Modifier> inferredModifiers;

    UnitType(String name, String plural) {
        this(name, plural, null);
    }

    UnitType(String name, String plural, CraftrUtil.Modifier[] inferredModifiers) {
        this.name = name;
        this.plural = plural;
        this.inferredModifiers = (inferredModifiers != null) ? Arrays.asList(inferredModifiers) : Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public String getPlural() {
        return plural;
    }

    public List<CraftrUtil.Modifier> getInferredModifiers() {
        return inferredModifiers;
    }
}
