package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public interface DataReference {
    ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext);
    NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext);

    BooleanResolution compare(FunctionSection section, ScoreComparison op, DataReference other, SemanticContext semanticContext);
}
