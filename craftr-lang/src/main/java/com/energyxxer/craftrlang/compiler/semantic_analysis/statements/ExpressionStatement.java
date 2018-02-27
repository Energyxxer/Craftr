package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    public ExpressionStatement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        super(pattern, semanticContext, function);
    }

    @Override
    public Value evaluate(Function function) {
        function.append(new FunctionComment("Expression statement"));
        function.append(new FunctionComment("    that is: " + pattern.toString()));
        function.append(new FunctionComment("    that resolves into: " + ExprResolver.analyzeValue(((TokenGroup) pattern).getContents()[0], semanticContext, null, function)));
        return null;
    }
}
