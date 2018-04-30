package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class SetupStatement extends Statement {
    public SetupStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        section.append(new FunctionComment("Setup statement"));
        return null;
    }
}
