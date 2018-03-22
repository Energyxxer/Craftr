package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class TagBooleanReference implements BooleanReference {
    private CraftrEntity entity;
    private String tag;

    public TagBooleanReference(CraftrEntity entity, String tag) {
        this.entity = entity;
        this.tag = tag;
    }

    @Override
    public BooleanResolution resolveBoolean(Function function, SemanticContext semanticContext, boolean negated) {
        return null;
    }
}
