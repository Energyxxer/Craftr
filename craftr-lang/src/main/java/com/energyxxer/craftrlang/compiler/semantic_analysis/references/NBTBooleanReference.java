package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanReference;

public class NBTBooleanReference implements BooleanReference {
    private final NBTReference nbt;
    private final String data;

    public NBTBooleanReference(NBTReference nbt, String data) {
        this.nbt = nbt;
        this.data = data;
    }
}
