package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {
    public ExpressionStatement(TokenPattern<?> pattern, Context context) {
        super(pattern, context);

        Value content = ExprAnalyzer.analyzeValue(pattern.find("EXPRESSION"), context);
        System.out.println(content + " at " + context);
    }
}
