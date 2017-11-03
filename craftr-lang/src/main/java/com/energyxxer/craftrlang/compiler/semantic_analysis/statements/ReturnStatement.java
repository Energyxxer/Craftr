package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class ReturnStatement extends Statement {

    public ReturnStatement(TokenPattern<?> pattern, Context context, MCFunction function) {
        super(pattern, context, function);
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        TokenPattern<?> rawValue = pattern.find("VALUE");
        if(rawValue != null) {
            Value value = ExprResolver.analyzeValue(rawValue, context, null, function, silent);
            boolean wasNull = value == null;
            if(value != null) value = value.unwrap(function);
            if(!wasNull && value == null) {
                return null;
            }
            if(value != null) {
                if(!value.isExplicit()) value = value.getDataType().createImplicit(value.getReference(), context);
                return value;
            }
        }
        //TODO return value
        return null;
    }
}
