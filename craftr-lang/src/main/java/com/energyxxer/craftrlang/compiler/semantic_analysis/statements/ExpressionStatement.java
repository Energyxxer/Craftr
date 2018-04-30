package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Expression;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    public ExpressionStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        Value value = ExprResolver.analyzeValue(pattern.find("EXPRESSION"), semanticContext, this.dataHolder, section);
        if(value instanceof Expression) value = ((Expression) value).unwrap(section, null);
        return value;
    }
}
