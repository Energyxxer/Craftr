package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

public abstract class ValueWrapper extends Value {

    public ValueWrapper(SemanticContext semanticContext) {
        super(semanticContext);
    }

    public ValueWrapper(DataReference reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    public abstract Value unwrap(FunctionSection section);

    public abstract DataType getDataType();

    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return unwrap(section).runOperation(operator, pattern, section, silent);
    }
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        return unwrap(section).runOperation(operator, operand, pattern, section, semanticContext, resultReference, silent);
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        throw new UnsupportedOperationException("Cannot get sub symbol table of value wrapper");
    }

    @Override
    public MethodLog getMethodLog() {
        throw new UnsupportedOperationException("Cannot get method log of value wrapper");
    }

    @Override
    public Value clone(Function function) {
        throw new UnsupportedOperationException("Cannot clone value wrapper");
    }
}
