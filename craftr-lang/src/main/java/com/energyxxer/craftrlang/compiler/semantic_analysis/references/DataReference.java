package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public interface DataReference {
    ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext);
    NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext);

    BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext);
}
