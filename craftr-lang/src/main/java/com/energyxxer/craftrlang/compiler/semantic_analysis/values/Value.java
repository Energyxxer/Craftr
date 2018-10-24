package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

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
        return reference instanceof ExplicitValue;
    }

    public boolean isImplicit() {
        return !(reference instanceof ExplicitValue);
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

    public abstract Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent);
    public abstract Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent);

    public Value runShorthandOperation(DataReference reference, Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, boolean silent) {
        return null;
    }

    public abstract Value clone(Function function);

    public boolean isNull() {
        return getDataType() != null && getDataType().isNullType();
    }
}
