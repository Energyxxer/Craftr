package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.Score;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public abstract class Value implements TraversableStructure, Score {
    protected final Context context;
    protected ObjectivePointer reference = null;

    public Value(Context context) {
        this.context = context;
    }

    public Value(ObjectivePointer reference, Context context) {
        this.reference = reference;
        this.context = context;
    }

    public boolean isExplicit() {
        return reference == null;
    }

    public void setReference(ObjectivePointer reference) {
        this.reference = reference;
    }

    public ObjectivePointer getReference() {
        return reference;
    }

    public int getScoreboardValue() {
        return Integer.MIN_VALUE;
    }

    public abstract DataType getDataType();
    public abstract SymbolTable getSubSymbolTable();
    public abstract MethodLog getMethodLog();

    public Value runOperation(Operator operator, TokenPattern<?> pattern, MCFunction function) {
        return runOperation(operator, pattern, function, false);
    }

    public Value runOperation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        Value returnValue = this.operation(operator, pattern, function, silent);
        if(returnValue == null) {
            this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to '" + getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, MCFunction function) {
        return runOperation(operator, value, pattern, function, false);
    }

    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        Value returnValue = this.unwrap(function).operation(operator, value.unwrap(function), pattern, function, silent);
        if(returnValue == null) {
            if(!silent) this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to types '" + getDataType() + " (" + this + ")', '" + value.getDataType() + "(" + value + ")'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value unwrap(MCFunction function) {
        return this;
    }

    protected abstract Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean silent);
    protected abstract Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean silent);
}
