package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 3/3/2017.
 */
public class SymbolTable implements Iterable<Symbol> {
    private final Compiler compiler;
    private final SymbolVisibility visibility;
    private SymbolTable parent = null;

    private HashMap<String, Symbol> table = new HashMap<>();

    public SymbolTable() {
        this.visibility = SymbolVisibility.GLOBAL;
        this.parent = null;
        this.compiler = null;
    }

    public SymbolTable(Compiler compiler) {
        this.visibility = SymbolVisibility.GLOBAL;
        this.parent = null;
        this.compiler = compiler;
    }

    public SymbolTable(SymbolVisibility visibility, SymbolTable parent) {
        this.visibility = visibility;
        this.parent = parent;
        this.compiler = parent.compiler;
    }

    public int getLevel() {
        return (parent != null) ? parent.getLevel()+1 : 0;
    }

    @Override
    public String toString() {
        return visibility.name() + ":" + getLevel() + ":" + table;
    }

    public void put(Symbol symbol) {
        table.put(symbol.getName(), symbol);
    }

    public void put(String name, Symbol symbol) {
        table.put(name, symbol);
    }

    public void putAll(SymbolTable other) {
        for(Symbol symbol : other.table.values()) {
            this.put(symbol);
        }
    }

    public boolean isRoot() {
        return parent == null;
    }

    public SymbolTable getRoot() {
        if(this.parent == null) return this;
        return parent.getRoot();
    }

    public Symbol getSymbol(List<Token> flatTokens, Context context) {
        return getSymbol(flatTokens, context, false);
    }

    public Symbol getSymbol(List<Token> flatTokens, Context context, boolean silent) {
        Token token = flatTokens.get(0);
        String raw = token.value;
        Symbol next = this.table.get(raw);
        if(next != null) {
            switch(next.getVisibility()) {
                case PACKAGE: {
                    if(context.getDeclaringFile().getPackage() != next.getPackage()) {
                        if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot access symbol '" + raw + "' from current context", token.getFormattedPath()));
                    }
                    break;
                }
                case UNIT: {
                    if(context.getContextType() != ContextType.UNIT || context != next.getUnit()) {
                        if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot access symbol '" + raw + "' from current context", token.getFormattedPath()));
                    }
                    break;
                }
                case UNIT_INHERITED: {
                    //TODO
                    break;
                }
                case METHOD: {
                    //TODO
                    break;
                }
                case BLOCK: {
                    //TODO
                    break;
                }
            }
            if(flatTokens.size() > 1) {
                SymbolTable subTable = next.getSubSymbolTable();
                if(subTable != null) {
                    return next.getSubSymbolTable().getSymbol(flatTokens.subList(2, flatTokens.size()), context, silent);
                }
                if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, raw + " is not a data structure", token.getFormattedPath()));
            }
            return next;
        }
        if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol '" + raw + "'", token.getFormattedPath()));
        return null;
    }

    public Symbol getSymbol(Token singleToken, Context context) {
        return getSymbol(Collections.singletonList(singleToken), context);
    }

    public Symbol getSymbol(Token singleToken, Context context, boolean silent) {
        return getSymbol(Collections.singletonList(singleToken), context, silent);
    }

    public SymbolTable mergeWith(SymbolTable other) {
        SymbolTable newTable = new SymbolTable();
        for(Symbol symbol : this.table.values()) {
            newTable.put(symbol);
        }
        for(Symbol symbol : other.table.values()) {
            newTable.put(symbol);
        }
        return newTable;
    }

    public SymbolTable duplicate() {
        SymbolTable newTable = new SymbolTable();
        for(Symbol symbol : this.table.values()) {
            newTable.put(symbol);
        }
        return newTable;
    }

    public HashMap<String, Symbol> getMap() {
        return table;
    }

    @NotNull
    @Override
    public Iterator<Symbol> iterator() {
        return table.values().iterator();
    }
}
