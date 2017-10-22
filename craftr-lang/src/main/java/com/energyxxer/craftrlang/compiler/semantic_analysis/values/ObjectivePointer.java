package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.commands.EntityReference;

public class ObjectivePointer {
    private EntityReference entity;
    private String objectiveName;

    public ObjectivePointer(EntityReference entity, String objectiveName) {
        this.entity = entity;
        this.objectiveName = objectiveName;
    }

    public EntityReference getEntity() {
        return entity;
    }

    public void setEntity(EntityReference entity) {
        this.entity = entity;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    @Override
    public String toString() {
        return entity + " : " + objectiveName;
    }
}
