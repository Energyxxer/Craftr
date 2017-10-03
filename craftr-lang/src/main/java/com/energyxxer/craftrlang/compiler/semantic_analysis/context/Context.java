package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;

/**
 * Created by User on 5/16/2017.
 */
public interface Context {
    CraftrFile getDeclaringFile();
    Unit getUnit();
    ContextType getContextType();
    SemanticAnalyzer getAnalyzer();
    boolean isStatic();

    Context getParent();
    SymbolTable getReferenceTable();

    default Symbol findSymbol(Token name) {
        return findSymbol(name, false);
    }

    default Symbol findSymbol(Token name, boolean silent) {
        Symbol inCurrent = (getReferenceTable() != null) ? getReferenceTable().getSymbol(name, this, silent) : null;
        if(inCurrent != null) return inCurrent;
        else if(getParent() != null) return getParent().findSymbol(name, silent);
        return null;
    }
}
