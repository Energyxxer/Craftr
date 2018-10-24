package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class CommentStatement extends Statement {

    private String comment;

    public CommentStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
        this.comment = ((TokenItem) pattern.find("FUNCTION_COMMENT_TOKEN")).getContents().value.substring(2);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        section.append(new FunctionComment(comment));
        return null;
    }

    @Override
    public boolean isExplicit() {
        return true;
    }
}
