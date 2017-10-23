package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.Comment;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ActionStatement extends Statement {
    public ActionStatement(TokenPattern<?> pattern, Context context, MCFunction function) {
        super(pattern, context, function);
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        function.addInstruction(new Comment("say action"));
        return null;
    }
}
