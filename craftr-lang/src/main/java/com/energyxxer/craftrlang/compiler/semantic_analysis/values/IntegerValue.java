package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class IntegerValue extends NumericalValue {

    private int value = 0;

    public IntegerValue(Context context) {
        this(0, context);
    }

    public IntegerValue(int value, Context context) {
        super(context);
        this.value = value;
    }

    public IntegerValue(LocalScore reference, Context context) {
        super(context);
        this.reference = reference;
    }

    /**
     * Coerces this <code>IntegerValue</code> to the numeric type of highest weight between itself and the parameter.
     * <br>
     * If <code>other</code> is the same type or lower, <code>this</code> is returned.
     * <br>
     * Otherwise, this value is converted to whatever type <code>other</code> is.
     * */
    @Override
    public NumericalValue coerce(NumericalValue other) {
        if(other instanceof IntegerValue) return this;
        if(other instanceof FloatValue) return new FloatValue(this.value, context);
        return null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        /* Note to future self:
         *     for incrementing, make sure to change this.value, then return clones of this value.
         */
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public DataType getDataType() {
        return DataType.INT;
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
    public String toString() {
        return "IntegerValue(" + ((this.isExplicit()) ? value : reference) + ")";
    }

    @Override
    public Integer getRawValue() {
        return this.value;
    }

    @Override
    public IntegerValue clone(Function function) {
        if(this.isExplicit()) {
            return new IntegerValue(this.value, context);
        } else {
            //TODO: THIS
            return null;
        }
    }
}
