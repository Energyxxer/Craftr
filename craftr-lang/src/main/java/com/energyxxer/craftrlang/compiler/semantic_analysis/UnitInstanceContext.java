package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

public class UnitInstanceContext implements SemanticContext {
    private final Unit unit;
    private final LocalizedObjectiveManager locObjMgr;

    public UnitInstanceContext(Unit unit) {
        this.unit = unit;
        locObjMgr = unit.getAnalyzer().getCompiler().getModule().createLocalizedObjectiveManager(this);
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
    public SymbolTable getReferenceTable() {
        return null; //WHAT TO DO
    }

    @Override
    public DataHolder getDataHolder() {
        return unit.getGenericInstance();
    }

    @Override
    public ObjectInstance getInstance() {
        return unit.getGenericInstance();
    }

    @Override
    public ScoreHolder getPlayer() {
        return unit.getGenericInstance().getEntity();
    }

    @Override
    public LocalizedObjectiveManager getLocalizedObjectiveManager() {
        return locObjMgr;
    }
}
