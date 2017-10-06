package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class ObjectInstance extends Value implements DataHolder {

    private Unit unit;

    private FieldLog fieldLog;
    private MethodLog methodLog;

    public ObjectInstance(Unit unit, Context context) {
        super(context);
        this.unit = unit;

        this.fieldLog = unit.getInstanceFieldLog().createForInstance(this);
        this.methodLog = unit.getInstanceMethodLog().createForInstance(this);
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return fieldLog.getFieldTable();
    }

    @Override
    public MethodLog getMethodLog() {
        return methodLog;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    public Unit getValue() {
        return this.unit;
    }
}
