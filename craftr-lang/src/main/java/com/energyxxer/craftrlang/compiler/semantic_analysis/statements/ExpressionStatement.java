package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    private FunctionWriter content;
    private boolean valid = true;

    public ExpressionStatement(TokenPattern<?> pattern, Context context, MCFunction function) {
        super(pattern, context, function);

        Value content = ExprResolver.analyzeValue(pattern.find("EXPRESSION"), context, null, function);
        if(content instanceof FunctionWriter) {
            this.content = (FunctionWriter) content;
        } else if(content != null) {
            valid = false;
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Not a statement", pattern.getFormattedPath()));
        } else valid = false;
        System.out.println(content + " at " + context);
        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Expression Report", NoticeType.INFO, content + " at " + context, pattern.getFormattedPath()));
    }

    @Override
    public void writeToFunction(MCFunction function) {

    }
}
