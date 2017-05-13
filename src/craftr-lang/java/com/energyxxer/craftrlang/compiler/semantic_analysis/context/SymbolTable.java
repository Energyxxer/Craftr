package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.util.out.Console;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by User on 3/3/2017.
 */
public class SymbolTable {
    private final SymbolVisibility visibility;
    private SymbolTable parent = null;

    private HashMap<String, Symbol> table = new HashMap<>();

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

    public void put(Symbol symbol) {
        table.put(symbol.getName(), symbol);
    }

    public void put(String name, Symbol symbol) {
        table.put(name, symbol);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public SymbolTable getRoot() {
        if(this.parent == null) return this;
        return parent.getRoot();
    }

    public Symbol getSymbol(String path) {
        String[] sections = path.split("\\.",2);
        Console.info.println(path);
        Console.info.println(Arrays.toString(sections));
        Symbol next = this.table.get(sections[0]);
        if(next != null) {
            if(sections.length > 1) {
                SymbolTable subTable = next.getSubSymbolTable();
                if(subTable != null) {
                    return next.getSubSymbolTable().getSymbol(sections[1]);
                }
                Console.err.println(next + " is not a symbol table");
                return null;
            }
            return next;
        }
        Console.err.println(sections[0] + " is not defined");
        return null;
    }

    public HashMap<String, Symbol> getMap() {
        return table;
    }
}
