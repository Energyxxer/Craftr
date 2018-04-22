package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ReturnStatement extends Statement {

    public ReturnStatement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        super(pattern, semanticContext, function);
    }

    @Override
    public Value evaluate(Function function) {
        TokenPattern<?> rawValue = pattern.find("VALUE");
        if(rawValue != null) return ExprResolver.analyzeValue(rawValue, semanticContext, dataHolder, function);
        return null;
    }
}
