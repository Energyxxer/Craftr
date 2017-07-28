package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class FieldManager {
    private final Unit parentUnit;

    private SymbolTable staticFields;
    private SymbolTable instanceFields;

    public FieldManager(Unit parentUnit) {
        this.parentUnit = parentUnit;

        this.staticFields = new SymbolTable(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
        this.instanceFields = new SymbolTable(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
    }

    public Variable findField(Token name) {
        Symbol symbol;
        symbol = staticFields.getMap().get(name.value);
        if(symbol != null && symbol instanceof Variable) return (Variable) symbol;
        symbol = instanceFields.getMap().get(name.value);
        if(symbol != null && symbol instanceof Variable) return (Variable) symbol;
        return null;
    }

    public void insertField(TokenStructure component) {
        Variable.parseDeclaration(component, this, parentUnit);
    }

    public Unit getParentUnit() {
        return parentUnit;
    }

    public SymbolTable getStaticFieldTable() {
        return staticFields;
    }

    public SymbolTable getInstanceFieldTable() {
        return instanceFields;
    }
}
