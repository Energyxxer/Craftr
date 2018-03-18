package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.entity.Entity;

public class TagBooleanReference implements BooleanReference {
    private Entity entity;
    private String tag;

    public TagBooleanReference(Entity entity, String tag) {
        this.entity = entity;
        this.tag = tag;
    }
}
