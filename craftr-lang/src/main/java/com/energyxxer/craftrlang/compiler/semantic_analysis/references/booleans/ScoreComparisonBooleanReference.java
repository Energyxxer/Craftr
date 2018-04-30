package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;

public class ScoreComparisonBooleanReference implements BooleanReference {
    private DataReference a;
    private ScoreComparison op;
    private DataReference b;

    public ScoreComparisonBooleanReference(DataReference a, DataReference b) {
        this(a, ScoreComparison.EQUAL, b);
    }

    public ScoreComparisonBooleanReference(DataReference a, ScoreComparison op, DataReference b) {
        this.a = a;
        this.op = op;
        this.b = b;
    }

    @Override
    public BooleanResolution resolveBoolean(FunctionSection section, SemanticContext semanticContext, boolean negated) {
        return a.compare(section, op, b, semanticContext);
    }
}
