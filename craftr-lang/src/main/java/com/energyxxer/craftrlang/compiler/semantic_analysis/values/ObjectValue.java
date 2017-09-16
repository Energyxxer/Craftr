package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/13/2017.
 */
public class ObjectValue extends Value {

    private Unit unit;

    public ObjectValue(Context context, Unit unit) {
        super(context);
        this.unit = unit;
    }

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return unit.getFieldManager().getInstanceFieldTable();
    }

    @Override
    public MethodManager getMethodManager() {
        return unit.getMethodManager();
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
