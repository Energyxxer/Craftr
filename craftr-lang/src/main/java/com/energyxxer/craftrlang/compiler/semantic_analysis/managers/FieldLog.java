package com.energyxxer.craftrlang.compiler.semantic_analysis.managers;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class FieldLog extends SymbolTable {
    private final Unit parentUnit;
    private final boolean isStatic;
    private ObjectInstance parentInstance;

    public FieldLog(Unit parentUnit) {
        super(parentUnit.getVisibility(), parentUnit.getDeclaringFile().getPackage().getSubSymbolTable());
        this.parentUnit = parentUnit;
        this.parentInstance = null;
        this.isStatic = true;
    }

    public FieldLog(ObjectInstance instance) {
        super(instance.getUnit().getVisibility(), instance.getUnit().getDeclaringFile().getPackage().getSubSymbolTable());
        this.parentUnit = instance.getUnit();
        this.parentInstance = instance;
        this.isStatic = false;

        for(Symbol symbol : parentUnit.getInstanceFieldLog().getMap().values()) {
            if(symbol instanceof Variable) {
                this.put(((Variable) symbol).duplicate());
            }
        }
    }

    @Override
    public Symbol getSymbol(List<Token> flatTokens, Context context, boolean silent) {

        if(flatTokens.size() > 1) {
            //I don't think this should even be allowed
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Trying to get a symbol of more than one token from a field log...?", flatTokens.get(0).getFormattedPath()));
            return super.getSymbol(flatTokens, context, silent);
        }

        Symbol sym = super.getSymbol(flatTokens, context, true);
        if(sym != null) return sym;
        if(!isStatic) sym = parentUnit.getStaticFieldLog().getSymbol(flatTokens, context, true);
        if(sym != null) return sym;

        List<Unit> im = parentUnit.getInheritanceMap();
        for(Unit parent : im) {
            Symbol sym2;

            if(isStatic) {
                sym2 = parent.getStaticFieldLog().getSymbol(flatTokens, context, true);
            } else {
                sym2 = parent.getInstanceFieldLog().getSymbol(flatTokens, context, true);
            }
            if(sym2 != null) return sym2;
        }
        if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol '" + flatTokens.get(0).value + "'", flatTokens.get(0).getFormattedPath()));
        return null;
    }

    /*public Variable findField(Token name) {
        Symbol symbol = this.getMap().get(name.value);
        return (symbol != null && symbol instanceof Variable) ? (Variable) symbol : ((parentLog != null) ? parentLog.findField(name) : null);
    }*/

    public void addField(Variable field) {
        this.put(field.getName(), field);
    }

    public FieldLog createForInstance(ObjectInstance instance) {
        return new FieldLog(instance);
    }

    public Unit getParentUnit() {
        return parentUnit;
    }

    public void forEachVar(Consumer<? super Variable> action) {
        for(Symbol symbol : this) {
            if(symbol instanceof Variable) {
                action.accept((Variable) symbol);
            }
        }
    }
}
