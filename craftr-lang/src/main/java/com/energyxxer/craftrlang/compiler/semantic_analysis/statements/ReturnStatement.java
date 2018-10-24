package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ReturnStatement extends Statement {

    public ReturnStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        TokenPattern<?> rawValue = pattern.find("VALUE");
        if(rawValue != null) return ExprResolver.analyzeValue(rawValue, semanticContext, dataHolder, section);
        return null;
    }

    @Override
    public boolean isExplicit() {
        TokenPattern<?> value = pattern.find("VALUE");
        return ExprResolver.analyzeValue(value, semanticContext, dataHolder, section).isExplicit();
    }
}
