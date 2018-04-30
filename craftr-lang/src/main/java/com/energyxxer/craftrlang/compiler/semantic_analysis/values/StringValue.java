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
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitString;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class StringValue extends Value {

    public StringValue(String value, SemanticContext semanticContext) {
        super(semanticContext);
        this.reference = new ExplicitString(value);
    }

    public StringValue(DataReference reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodLog getMethodLog() {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return "StringValue(" + reference + ")";
    }

    @Override
    public StringValue clone(Function function) {
        return new StringValue(reference, semanticContext);
    }
}
