package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTCompoundBuilder;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.TagString;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

public class ExplicitString implements ExplicitValue {

    private final String value;

    public ExplicitString(String value) {
        this.value = value;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score) {
        throw new UnsupportedOperationException("Cannot set a score's value to a string");
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path) {
        NBTCompoundBuilder cb = new NBTCompoundBuilder();

        cb.put(path, new TagString(value));

        return new NBTReference(entity, path);
    }
}
