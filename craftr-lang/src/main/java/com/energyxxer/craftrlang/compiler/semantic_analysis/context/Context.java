package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.code_generation.objectives.ResolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.code_generation.players.Player;
import com.energyxxer.craftrlang.compiler.code_generation.players.PlayerReference;
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

    Player getPlayer();
    default PlayerReference getPlayerReference() {
        Player player = getPlayer();
        return (player != null) ? new PlayerReference(player, "@s") : null;
    }

    default PlayerReference resolve(Player player) {
        Player thisPlayer = getPlayer();
        return (thisPlayer != null) ? thisPlayer.resolvePlayer(player) : null;
    }

    default ResolvedObjectiveReference resolve(UnresolvedObjectiveReference reference) {
        return reference.resolveTo(this.resolve(reference.getPlayer()));
    }
}
