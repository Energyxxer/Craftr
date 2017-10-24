package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public abstract class Statement implements FunctionWriter {
    protected final TokenPattern<?> pattern;
    protected final Context context;
    protected final MCFunction function;

    protected boolean silent = false;

    public Statement(TokenPattern<?> pattern, Context context, MCFunction function) {
        this.pattern = pattern;
        this.context = context;
        this.function = function;
    }

    public static Statement read(TokenPattern<?> pattern, Context context, MCFunction function) {
        switch(pattern.getName()) {
            case "EXPRESSION_STATEMENT": return new ExpressionStatement(pattern, context, function);
            case "SETUP_STATEMENT": return new SetupStatement(pattern, context, function);
            case "DELIMITED_CODE_BLOCK": return new CodeBlock(pattern, context); //<-- Do something about this maybe?
            case "ACTION_STATEMENT": return new ActionStatement(pattern, context, function);
            case "RETURN_STATEMENT": return new ReturnStatement(pattern, context, function);
            case "IF_STATEMENT": return new IfStatement(pattern, context, function);
            default: return null;
        }
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
