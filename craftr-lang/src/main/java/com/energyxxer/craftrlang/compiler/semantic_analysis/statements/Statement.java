package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public abstract class Statement {
    protected final TokenPattern<?> pattern;
    protected final SemanticContext semanticContext;
    protected final FunctionSection section;
    protected DataHolder dataHolder;

    protected boolean silent = false;

    public Statement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        this(pattern, semanticContext, section, null);
    }

    public Statement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section, DataHolder dataHolder) {
        this.pattern = pattern;
        this.semanticContext = semanticContext;
        this.section = section;
        this.dataHolder = dataHolder;
    }

    public static Statement read(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        switch(pattern.getName()) {
            case "EXPRESSION_STATEMENT": return new ExpressionStatement(pattern, semanticContext, section);
            case "SETUP_STATEMENT": return new SetupStatement(pattern, semanticContext, section);
            case "DELIMITED_CODE_BLOCK": return new CodeBlock(pattern, semanticContext); //<-- Do something about this maybe?
            case "ACTION_STATEMENT": return new ActionStatement(pattern, semanticContext, section);
            case "RETURN_STATEMENT": return new ReturnStatement(pattern, semanticContext, section);
            case "IF_STATEMENT": return new IfStatement(pattern, semanticContext, section);
            case "SWITCH_STATEMENT": return new SwitchStatement(pattern, semanticContext, section);
            case "FOR_STATEMENT": return new ForStatement(pattern, semanticContext, section);
            case "WHILE_STATEMENT": return new WhileStatement(pattern, semanticContext, section);
            case "VARIABLE": return new VariableDeclaration(pattern, semanticContext, section);
            case "DEBUG_STATEMENT": return new DebugStatement(pattern, semanticContext, section);
            default: return null;
        }
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public abstract Value evaluate(FunctionSection section);
}
