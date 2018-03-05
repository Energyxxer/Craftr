package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

public class FieldInitContext implements SemanticContext {
    private final Unit unit;
    private final LocalizedObjectiveManager locObjMgr;

    private ObjectInstance ownerInstance;

    //private final CraftrEntity entity;

    public FieldInitContext(Unit unit) {
        this.unit = unit;
        this.locObjMgr = unit.getAnalyzer().getCompiler().getModule().createLocalizedObjectiveManager(this);

        //this.entity = new CraftrEntity(unit, new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument(getCompiler().getPrefix() + "_init"))); //TODO Make this a getter in the Unit class so it can be made the sender of the initialization function
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return unit.getDeclaringFile();
    }

    @Override
    public Unit getUnit() {
        return unit;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.UNIT;
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return unit.getAnalyzer();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public SemanticContext getParent() {
        return unit;
    }

    @Override
    public LocalizedObjectiveManager getLocalizedObjectiveManager() {
        return locObjMgr;
    }

    public ObjectInstance getOwnerInstance() {
        if(ownerInstance == null) ownerInstance = new ObjectInstance(unit, this);
        return ownerInstance;
    }

    @Override
    public DataHolder getDataHolder() {
        return getOwnerInstance();
    }

    @Override
    public ScoreHolder getPlayer() {
        return getOwnerInstance().getEntity();
    }

    @Override
    public String toString() {
        return "FieldInitContext for " + unit.getFullyQualifiedName();
    }
}
