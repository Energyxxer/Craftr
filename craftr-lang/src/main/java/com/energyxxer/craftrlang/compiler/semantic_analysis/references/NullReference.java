package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.data.DataMergeCommand;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTCompoundBuilder;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.TagInt;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public class NullReference implements DataReference {
    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        function.append(new ScoreSet(score, 0));
        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        NBTCompoundBuilder cb = new NBTCompoundBuilder();
        cb.put(path, new TagInt(0));

        function.append(new DataMergeCommand(entity, cb.getCompound()));
        return new NBTReference(entity, path);
    }

    @Override
    public BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        return null;
    }
}
