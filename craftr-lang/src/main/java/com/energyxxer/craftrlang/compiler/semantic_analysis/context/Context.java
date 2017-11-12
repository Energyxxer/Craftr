package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.code_generation.objectives.ResolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolder;
import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolderReference;
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
    default ScoreHolderReference getPlayerReference() {
        ScoreHolder scoreHolder = getPlayer();
        return (scoreHolder != null) ? new ScoreHolderReference(scoreHolder, "@s") : null;
    }

    default ScoreHolderReference resolve(ScoreHolder scoreHolder) {
        ScoreHolder thisScoreHolder = getPlayer();
        return (thisScoreHolder != null) ? thisScoreHolder.resolvePlayer(scoreHolder) : null;
    }

    default ResolvedObjectiveReference resolve(UnresolvedObjectiveReference reference) {
        return reference.resolveTo(this.resolve(reference.getScoreHolder()));
    }
}
