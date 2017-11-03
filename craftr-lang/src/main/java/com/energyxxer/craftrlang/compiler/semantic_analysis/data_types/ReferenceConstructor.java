package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public interface ReferenceConstructor {
    Value create(UnresolvedObjectiveReference reference, Context context);
}
