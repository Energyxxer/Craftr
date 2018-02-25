package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.commands.scoreboard.ScoreSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTCompoundBuilder;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.TagFloat;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

public class ExplicitFloat implements ExplicitValue {

    private final float value;

    public ExplicitFloat(float value) {
        this.value = value;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score) {
        function.append(new ScoreSet(score, (int) value));
        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path) {
        NBTCompoundBuilder cb = new NBTCompoundBuilder();

        cb.put(path, new TagFloat(value));

        return new NBTReference(entity, path);
    }
}
