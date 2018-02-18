package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.codegen.CommandWriter;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public abstract class Statement implements CommandWriter {
    protected final TokenPattern<?> pattern;
    protected final Context context;
    protected final Function function;

    protected boolean silent = false;

    public Statement(TokenPattern<?> pattern, Context context, Function function) {
        this.pattern = pattern;
        this.context = context;
        this.function = function;
    }

    public static Statement read(TokenPattern<?> pattern, Context context, Function function) {
        switch(pattern.getName()) {
            case "EXPRESSION_STATEMENT": return new ExpressionStatement(pattern, context, function);
            case "SETUP_STATEMENT": return new SetupStatement(pattern, context, function);
            case "DELIMITED_CODE_BLOCK": return new CodeBlock(pattern, context); //<-- Do something about this maybe?
            case "ACTION_STATEMENT": return new ActionStatement(pattern, context, function);
            case "RETURN_STATEMENT": return new ReturnStatement(pattern, context, function);
            case "IF_STATEMENT": return new IfStatement(pattern, context, function);
            case "SWITCH_STATEMENT": return new SwitchStatement(pattern, context, function);
            case "FOR_STATEMENT": return new ForStatement(pattern, context, function);
            case "WHILE_STATEMENT": return new WhileStatement(pattern, context, function);
            case "VARIABLE": return new VariableDeclaration(pattern, context, function);
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
