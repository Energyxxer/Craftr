package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompoundSymbolTable extends SymbolTable {
    private ArrayList<SymbolTable> tables = new ArrayList<>();

    public CompoundSymbolTable(SymbolTable... tables) {
        this(Arrays.asList(tables));
    }

    public CompoundSymbolTable(List<SymbolTable> tables) {
        this.tables.addAll(tables);
    }

    public CompoundSymbolTable() {
    }

    @Override
    public Symbol getSymbol(List<Token> flatTokens, Context context, boolean silent) {
        Symbol sym = super.getSymbol(flatTokens, context, true);
        if(sym != null) return sym;
        for(int i = 0; i < tables.size(); i++) {
            sym = tables.get(i).getSymbol(flatTokens, context, (i < tables.size()-1) || silent);
            if(sym != null) return sym;
        }
        return null;
    }


}
