package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public abstract class Value implements TraversableStructure {
    protected final SemanticContext semanticContext;
    protected DataReference reference = null;

    public Value(SemanticContext semanticContext) {
        this.semanticContext = semanticContext;
    }

    public Value(DataReference reference, SemanticContext semanticContext) {
        this.reference = reference;
        this.semanticContext = semanticContext;
    }

    public boolean isExplicit() {
        return reference == null;
    }

    public void setReference(DataReference reference) {
        this.reference = reference;
    }

    public DataReference getReference() {
        return reference;
    }

    public abstract DataType getDataType();
    public abstract SymbolTable getSubSymbolTable();
    public abstract MethodLog getMethodLog();

    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        Value returnValue = this.operation(operator, pattern, function, fromVariable, silent);
        if(returnValue == null) {
            this.semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to '" + getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {

        Value thisUnwrapped = this.unwrap(function);
        Value operandUnwrapped = value.unwrap(function);

        // Unbox variables on the right-hand side
        if(operandUnwrapped != null && operandUnwrapped instanceof Variable) operandUnwrapped = ((Variable) operandUnwrapped).getValue();

        if(thisUnwrapped == null || operandUnwrapped == null) return null;

        Value returnValue = thisUnwrapped.operation(operator, operandUnwrapped, pattern, function, fromVariable, silent);
        if(returnValue == null) {
            if(!silent) this.semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to types '" + thisUnwrapped.getDataType() + "', '" + operandUnwrapped.getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value unwrap(Function function) {
        return this;
    }

    protected abstract Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent);
    protected abstract Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent);

    public abstract Value clone(Function function);

    public boolean isNull() {
        return getDataType() != null && getDataType().isNullType();
    }
}
