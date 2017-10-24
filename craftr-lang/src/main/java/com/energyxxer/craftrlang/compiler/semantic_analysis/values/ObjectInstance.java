package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class ObjectInstance extends Value implements Symbol, DataHolder {

    private Unit unit;

    private FieldLog fieldLog;
    private MethodLog methodLog;

    public ObjectInstance(Unit unit, Context context) {
        this(unit, null, context);
    }

    public ObjectInstance(Unit unit, ObjectivePointer reference, Context context) {
        super(reference, context);
        this.unit = unit;

        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this);
        this.methodLog = unit.getInstanceMethodLog().createForInstance(this);

        this.fieldLog.put("this", this);
    }

    public @NotNull Unit getUnit() {
        return unit;
    }

    @Override
    public DataType getDataType() {
        return unit.getDataType();
    }

    @Override
    public @NotNull SymbolTable getSubSymbolTable() {
        return fieldLog;
    }

    @Override
    public MethodLog getMethodLog() {
        return methodLog;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        return null;
    }

    @Override
    public String getName() {
        return "<instance of " + unit.getFullyQualifiedName() + ">";
    }

    @Override
    public SymbolVisibility getVisibility() {
        return SymbolVisibility.UNIT;
    }
}
