package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public abstract class Statement {
    protected final TokenPattern<?> pattern;
    protected final Context context;

    public Statement(TokenPattern<?> pattern, Context context) {
        this.pattern = pattern;
        this.context = context;
    }

    public static Statement read(TokenPattern<?> pattern, Context context) {
        switch(pattern.getName()) {
            case "EXPRESSION_STATEMENT": return new ExpressionStatement(pattern, context);
            case "SETUP_STATEMENT": return new SetupStatement(pattern, context);
            case "DELIMITED_CODE_BLOCK": return new CodeBlock(pattern, context);
            case "ACTION_STATEMENT": return new ActionStatement(pattern, context);
            case "RETURN_STATEMENT": return new ReturnStatement(pattern, context);
            case "IF_STATEMENT": return new IfStatement(pattern, context);
            default: return null;
        }
    }
}
