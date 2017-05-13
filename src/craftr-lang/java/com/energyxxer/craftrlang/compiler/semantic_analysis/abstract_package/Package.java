package com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created by User on 4/9/2017.
 */
public class Package implements Symbol {
    //Null if root
    private Package parent;
    private String name;

    private HashMap<String, Package> subPackages = new HashMap<>();
    private HashMap<String, Unit> units = new HashMap<>();

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
        this.units.put(unit.getName(), unit);
        symbolTable.put(unit);
    }

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

    public SymbolTable getSubSymbolTable() {
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
