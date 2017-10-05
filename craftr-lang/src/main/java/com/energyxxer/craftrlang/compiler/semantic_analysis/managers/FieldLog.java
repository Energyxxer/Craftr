package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class FieldLog {
    private final Unit parentUnit;
    private ObjectInstance parentInstance;

    private SymbolTable fields;

    public FieldLog(Unit parentUnit) {
        this.parentUnit = parentUnit;
        this.parentInstance = null;

        this.fields = new SymbolTable(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
    }

    public FieldLog(ObjectInstance instance) {
        this.parentUnit = instance.getUnit();
        this.parentInstance = instance;

        this.fields = new SymbolTable(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
        for(Symbol symbol : parentUnit.getInstanceFieldLog().getFieldTable().getMap().values()) {
            if(symbol instanceof Variable) {
                this.fields.put(((Variable) symbol).duplicate());
            }
        }
    }

    public Variable findField(Token name) {
        Symbol symbol;
        symbol = fields.getMap().get(name.value);
        return (symbol != null && symbol instanceof Variable) ? (Variable) symbol : null;
    }

    public void addField(Variable field) {
        this.fields.put(field.getName(), field);
    }

    public FieldLog createForInstance(ObjectInstance instance) {
        return new FieldLog(instance);
    }

    public Unit getParentUnit() {
        return parentUnit;
    }

    public SymbolTable getFieldTable() {
        return fields;
    }
}
