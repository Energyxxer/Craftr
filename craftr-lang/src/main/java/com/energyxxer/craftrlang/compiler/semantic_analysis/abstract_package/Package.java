package com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by User on 4/9/2017.
 */
public class Package implements Symbol {
    private Package parent; //Null if root
    private String name;

    private HashMap<String, Package> subPackages = new HashMap<>();

    private SymbolTable symbolTable;

    Package(SymbolTable rootTable) {
        this.parent = null;
        this.name = "root";
        this.symbolTable = rootTable;
    }

    Package(@NotNull Package parent, String name) {
        this.parent = parent;
        this.name = name;
        this.symbolTable = new SymbolTable(SymbolVisibility.GLOBAL, parent.symbolTable);
        parent.symbolTable.put(this);
    }

    Package createPackage(String path) {
        if(!CraftrUtil.isValidIdentifierPath(name)) throw new IllegalArgumentException("'" + name + "' is not a valid package identifier path.");
        String[] sections = path.split("\\.",2);
        String name = sections[0];
        if(subPackages.containsKey(name)) return (sections.length > 1) ? subPackages.get(name).createPackage(sections[1]) : subPackages.get(name);
        Package newPackage = new Package(this, name);
        subPackages.put(name, newPackage);
        return (sections.length > 1) ? newPackage.createPackage(sections[1]) : newPackage;
    }

    public void addUnit(Unit unit) {
        symbolTable.put(unit);
    }

    public Collection<Symbol> getAllSubSymbols() {
        HashMap<String, Symbol> map = new HashMap<>();
        getAllSubSymbols(map);
        return map.values();
    }

    private void getAllSubSymbols(HashMap<String, Symbol> map) {
        for(Symbol sym : this.symbolTable.getMap().values()) {
            if(!(sym instanceof Package) && !map.containsKey(sym.getName())) map.put(sym.getName(), sym);
        }
        for(Package subPackage : this.subPackages.values()) {
            subPackage.getAllSubSymbols(map);
        }
    }

    /*void clone(SymbolTable rootTable) {
        Package clone = new Package(rootTable);
        clone.addAll(this);
    }

    TODO

    void clone(@NotNull Package parent) {
        Package clone = new Package(parent, this.name);
        clone.addAll(this);
    }

    void addAll(Package pack) {
        this.subPackages.putAll(pack.subPackages);
        this.symbolTable.putAll(this.symbolTable);
    }*/

    @Override
    public String getName() {
        return name;
    }

    public String getFullyQualifiedName() {
        return (parent != null) ? parent.getFullyQualifiedName() + ((!parent.isRoot()) ? '.' : "") + name : "";
    }

    @Override
    public SymbolVisibility getVisibility() {
        return SymbolVisibility.GLOBAL;
    }

    @Override
    public @NotNull SymbolTable getSubSymbolTable() {
        return symbolTable;
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public Package getRoot() {
        if(this.parent == null) return this;
        return parent.getRoot();
    }
}
