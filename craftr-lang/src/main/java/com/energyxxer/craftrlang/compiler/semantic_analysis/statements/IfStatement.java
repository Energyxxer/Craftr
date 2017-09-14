package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public class IfStatement extends Statement {
    public IfStatement(TokenPattern<?> pattern, Context context) {
        super(pattern, context);
    }
}
