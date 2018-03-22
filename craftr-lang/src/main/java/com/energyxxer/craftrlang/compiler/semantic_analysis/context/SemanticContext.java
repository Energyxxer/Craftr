package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.commodore.module.Namespace;
import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;
import com.energyxxer.craftrlang.compiler.codegen.objectives.GlobalObjectiveManager;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;

/**
 * Created by User on 5/16/2017.
 */
public interface SemanticContext {
    CraftrFile getDeclaringFile();
    Unit getUnit();
    ContextType getContextType();
    SemanticAnalyzer getAnalyzer();
    boolean isStatic();
    SemanticContext getParent();

    LocalizedObjectiveManager getLocalizedObjectiveManager();

    DataHolder getDataHolder();

    default ObjectInstance getInstance() {
        return null;
    }
    ScoreHolder getScoreHolder();

    default Compiler getCompiler() {
        return getAnalyzer().getCompiler();
    }
    default CraftrCommandModule getModule() {
        return getAnalyzer().getCompiler().getModule();
    }
    default GlobalObjectiveManager getGlobalObjectiveManager() {
        return getModule().getGlobalObjectiveManager();
    }
    default Namespace getModuleNamespace() {
        return getModule().projectNS;
    }
}
