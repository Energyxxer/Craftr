package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Expression;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    private Expression content;
    private boolean valid = true;

    public ExpressionStatement(TokenPattern<?> pattern, Context context, MCFunction function) {
        super(pattern, context, function);

        Value content = ExprResolver.analyzeValue(pattern.find("EXPRESSION"), context, function);
        this.content = (Expression) content;
        /*if(content instanceof Expression) {
        } else {
            valid = false;
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Not a statement", pattern.getFormattedPath()));
        }*/
        System.out.println(content + " at " + context);
    }

    @Override
    public void writeToFunction(MCFunction function) {

    }
}
