package com.energyxxer.craftrlang.compiler.semantic_analysis.natives;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.ActualParameter;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.HashMap;
import java.util.List;

public interface MethodExecutor {
    Value writeCall(Function function, List<ActualParameter> positionalParams, HashMap<String, ActualParameter> keywordParams, TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder);
}
