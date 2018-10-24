package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.enxlex.lexical_analysis.token.Token;
import com.energyxxer.enxlex.report.Notice;
import com.energyxxer.enxlex.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class FieldLog extends SymbolTable {
    private final Unit parentUnit;
    private final boolean isStatic;
    private ObjectInstance parentInstance;

    private ArrayList<Symbol> orderedSymbols = new ArrayList<>();

    public FieldLog(Unit parentUnit) {
        super(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
        this.parentUnit = parentUnit;
        this.parentInstance = null;
        this.isStatic = true;
    }

    public FieldLog(ObjectInstance instance, boolean initialized) {
        super(instance.getUnit().getVisibility(), instance.getUnit().getDeclaringFile().getPackage().getSubSymbolTable());
        this.parentUnit = instance.getUnit();
        this.parentInstance = instance;
        this.isStatic = false;

        for(Symbol symbol : parentUnit.getInstanceFieldLog().getMap().values()) {
            if(symbol instanceof Variable) {
                this.put((initialized) ? ((Variable) symbol).createNew(instance) : ((Variable) symbol).createEmpty(instance));
            }
        }
    }

    @Override
    public Symbol getSymbol(List<Token> flatTokens, SemanticContext semanticContext, boolean silent) {

        if(flatTokens.size() > 1) {
            //I don't think this should even be allowed
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Trying to get a symbol of more than one token from a field log...?", flatTokens.get(0)));
            return super.getSymbol(flatTokens, semanticContext, silent);
        }

        Symbol sym = super.getSymbol(flatTokens, semanticContext, true);
        if(sym != null) return sym;
        if(!isStatic) sym = parentUnit.getStaticFieldLog().getSymbol(flatTokens, semanticContext, true);
        if(sym != null) return sym;

        List<Unit> im = parentUnit.getInheritanceMap();
        for(Unit parent : im) {
            Symbol sym2;

            if(isStatic) {
                sym2 = parent.getStaticFieldLog().getSymbol(flatTokens, semanticContext, true);
            } else {
                sym2 = parent.getInstanceFieldLog().getSymbol(flatTokens, semanticContext, true);
            }
            if(sym2 != null) return sym2;
        }
        if(!silent) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol '" + flatTokens.get(0).value + "'", flatTokens.get(0)));
        }
        return null;
    }

    /*public Variable findField(Token name) {
        Symbol symbol = this.getMap().get(name.value);
        return (symbol != null && symbol instanceof Variable) ? (Variable) symbol : ((parentLog != null) ? parentLog.findField(name) : null);
    }*/

    public void addField(Variable field) {
        this.put(field.getName(), field);
    }

    @Override
    public void put(String name, Symbol symbol) {
        super.put(name, symbol);
        this.orderedSymbols.add(symbol);
    }

    public FieldLog createForInstance(ObjectInstance instance, boolean initialized) {
        return new FieldLog(instance, initialized);
    }

    public Unit getParentUnit() {
        return parentUnit;
    }

    public void forEachVar(Consumer<? super Variable> action) {
        for(Symbol symbol : orderedSymbols) {
            if(symbol instanceof Variable) {
                action.accept((Variable) symbol);
            }
        }
    }

    @Override
    public String toString() {
        return (isStatic ? "Static" : "Instance") + " FieldLog for " + (isStatic ? "unit" : "instance of unit") + " " + parentUnit.getFullyQualifiedName() + (isStatic ? "" : " " + parentInstance);
    }
}
