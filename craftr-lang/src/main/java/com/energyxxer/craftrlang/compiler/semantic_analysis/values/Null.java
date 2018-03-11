package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NullReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

/**
 * null
 * */
public class Null extends Value {

    public Null(SemanticContext semanticContext) {
        super(new NullReference(), semanticContext);
    }

    @Override
    public DataType getDataType() {
        return DataType.NULL;
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
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        return null;
    }

    @Override
    public Value clone(Function function) {
        return null;
    }

    @Override
    public boolean isExplicit() {
        return true;
    }

    @Override
    public void setReference(DataReference reference) {
        //null
    }

    @Override
    public String toString() {
        return "<null>";
    }
}
