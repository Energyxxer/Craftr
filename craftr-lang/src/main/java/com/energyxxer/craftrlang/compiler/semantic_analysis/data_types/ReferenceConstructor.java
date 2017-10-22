package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public interface ReferenceConstructor {
    Value create(ObjectivePointer reference, Context context);
}
