package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ForStatement extends Statement {
    public ForStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        section.append(new FunctionComment("For statement"));
        return null;
    }

    @Override
    public boolean isExplicit() {
        return true;
    }
}
