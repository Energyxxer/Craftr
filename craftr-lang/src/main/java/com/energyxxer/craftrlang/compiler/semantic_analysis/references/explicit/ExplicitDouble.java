package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.commands.scoreboard.ScoreSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTCompoundBuilder;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.TagDouble;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

public class ExplicitDouble implements ExplicitValue {

    private final double value;

    public ExplicitDouble(double value) {
        this.value = value;
    }

    @Override
    public ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext) {
        section.append(new ScoreSet(score, (int) value));
        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext) {
        NBTCompoundBuilder cb = new NBTCompoundBuilder();

        cb.put(path, new TagDouble(value));

        return new NBTReference(entity, path);
    }

    public double getValue() {
        return value;
    }

    @Override
    public Number asNumber() {
        return getValue();
    }

    @Override
    public String toString() {
        return value + "d";
    }
}
