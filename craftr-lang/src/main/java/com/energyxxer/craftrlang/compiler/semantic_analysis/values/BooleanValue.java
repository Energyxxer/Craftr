package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator.NOT;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class BooleanValue extends Value {

    private boolean value = false;

    public BooleanValue(boolean value, Context context) {
        super(context);
        this.value = value;
    }

    public BooleanValue(LocalScore reference, Context context) {
        super(reference, context);
    }

    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
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
    public Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return (operator == NOT) ? new BooleanValue(!this.value, context) : null;
    }

    @Override
    public Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {

        if(operator == Operator.ASSIGN) {
            if(operand instanceof BooleanValue) {
                this.value = ((BooleanValue) operand).value;
                this.reference = operand.clone(function).getReference();
                return this.clone(function);
            } else {
                if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern.getFormattedPath()));
                return null;
            }
        }

        if(operand instanceof BooleanValue) switch(operator) {
            case AND:
                return new BooleanValue(this.value && ((BooleanValue) operand).value, this.context);
            case OR:
                return new BooleanValue(this.value || ((BooleanValue) operand).value, this.context);
            case EQUAL:
                return new BooleanValue(this.value == ((BooleanValue) operand).value, this.context);
        }

        return null;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public BooleanValue clone(Function function) {
        if(this.isExplicit()) {
            return new BooleanValue(this.value, context);
        } else {
            //TODO
            return null;
        }
    }
}
