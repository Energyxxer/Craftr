package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.commodore.module.Namespace;
import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

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
    DataHolder getDataHolder();

    default ObjectInstance getInstance() {
        return null;
    }
    ScoreHolder getPlayer();

    default Compiler getCompiler() {
        return getAnalyzer().getCompiler();
    }

    default CraftrCommandModule getModule() {
        return getAnalyzer().getCompiler().getModule();
    }

    default Namespace getModuleNamespace() {
        return getModule().projectNS;
    }
}
