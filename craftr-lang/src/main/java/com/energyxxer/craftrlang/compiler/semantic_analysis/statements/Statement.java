package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public abstract class Statement {
    protected final TokenPattern<?> pattern;
    protected final SemanticContext semanticContext;
    protected final Function function;

    protected boolean silent = false;

    public Statement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        this.pattern = pattern;
        this.semanticContext = semanticContext;
        this.function = function;
    }

    public static Statement read(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        switch(pattern.getName()) {
            case "EXPRESSION_STATEMENT": return new ExpressionStatement(pattern, semanticContext, function);
            case "SETUP_STATEMENT": return new SetupStatement(pattern, semanticContext, function);
            case "DELIMITED_CODE_BLOCK": return new CodeBlock(pattern, semanticContext); //<-- Do something about this maybe?
            case "ACTION_STATEMENT": return new ActionStatement(pattern, semanticContext, function);
            case "RETURN_STATEMENT": return new ReturnStatement(pattern, semanticContext, function);
            case "IF_STATEMENT": return new IfStatement(pattern, semanticContext, function);
            case "SWITCH_STATEMENT": return new SwitchStatement(pattern, semanticContext, function);
            case "FOR_STATEMENT": return new ForStatement(pattern, semanticContext, function);
            case "WHILE_STATEMENT": return new WhileStatement(pattern, semanticContext, function);
            case "VARIABLE": return new VariableDeclaration(pattern, semanticContext, function);
            default: return null;
        }
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public abstract Value evaluate(Function function);
}
