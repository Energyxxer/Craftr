package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public class ExplicitBoolean implements ExplicitValue, BooleanReference {
    private boolean value;

    public ExplicitBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public BooleanResolution resolveBoolean(FunctionSection section, SemanticContext semanticContext, boolean negated) {
        return new BooleanResolution(value);
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Number asNumber() {
        return null;
    }

    @Override
    public BooleanResolution compare(FunctionSection section, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        return BooleanReference.super.compare(section, op, other, semanticContext);
    }

    @Override
    public String toString() {
        return "boolean: " + value;
    }
}
