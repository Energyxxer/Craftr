package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.codegen.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.codegen.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ExpressionStatement extends Statement {

    private FunctionWriter content;

    public ExpressionStatement(TokenPattern<?> pattern, Context context, MCFunction function) {
        super(pattern, context, function);
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        Value rawContent = ExprResolver.analyzeValue(pattern.find("EXPRESSION"), context, null, function, silent);
        boolean valid = false;

        if(rawContent instanceof FunctionWriter) {
            this.content = (FunctionWriter) rawContent;
            valid = true;
        } else if(rawContent != null) {
            //if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Not a statement: " + rawContent, pattern.getFormattedPath()));
        }
        if(!silent) System.out.println(rawContent + " at " + context);
        if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Expression Report", NoticeType.INFO, rawContent + " at " + context, pattern.getFormattedPath()));

        if(content != null && valid) {
            return content.writeToFunction(function);
        }
        return null;
    }
}
