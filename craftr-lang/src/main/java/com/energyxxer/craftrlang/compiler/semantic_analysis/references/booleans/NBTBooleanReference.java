package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTTag;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;

public class NBTBooleanReference implements BooleanReference {
    private final NBTReference nbt;
    //need to add operation or sth
    private final NBTTag data;

    public NBTBooleanReference(NBTReference nbt, NBTTag data) {
        this.nbt = nbt;
        this.data = data;
    }

    @Override
    public BooleanResolution resolveBoolean(Function function, SemanticContext semanticContext, boolean negated) {
        return null;
    }
}
