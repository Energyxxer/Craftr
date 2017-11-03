package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * null
 * */
public class Null extends Value {

    public Null(Context context) {
        super(context);
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
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public Value clone(MCFunction function) {
        return null;
    }

    @Override
    public boolean isExplicit() {
        return true;
    }

    @Override
    public void setReference(UnresolvedObjectiveReference reference) {
        //null
    }

    @Override
    public UnresolvedObjectiveReference getReference() {
        return null;
    }

    @Override
    public int getScoreboardValue() {
        return Integer.MIN_VALUE;
    }

    @Override
    public String toString() {
        return "<null>";
    }
}
