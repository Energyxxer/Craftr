package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Expression;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    public ExpressionStatement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        super(pattern, semanticContext, function);
    }

    @Override
    public Value evaluate(Function function) {
        Value value = ExprResolver.analyzeValue(pattern.find("EXPRESSION"), semanticContext, null, function);
        if(value instanceof Expression) value = ((Expression) value).unwrap(function, null);
        return value;
    }
}
