package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

/**
 * Created by User on 3/3/2017.
 */
public class SymbolTable {
    private final SymbolVisibility visibility;
    private SymbolTable parent = null;

    public SymbolTable() {
        this(SymbolVisibility.GLOBAL, null);
    }

    public SymbolTable(SymbolVisibility visibility, SymbolTable parent) {
        this.visibility = visibility;
        this.parent = parent;
    }

    public int getLevel() {
        return (parent != null) ? parent.getLevel()+1 : 0;
    }

    @Override
    public String toString() {
        return visibility.name() + ":" + getLevel();
    }
}
