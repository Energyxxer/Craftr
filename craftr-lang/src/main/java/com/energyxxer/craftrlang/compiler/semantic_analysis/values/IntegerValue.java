package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.commands.SelectorReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.util.out.Console;

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

    public IntegerValue(ObjectivePointer reference, Context context) {
        super(context);
        this.reference = reference;
    }

    @Override
    public NumericalValue coerce(NumericalValue other) {
        if(other instanceof IntegerValue) return this;
        if(other instanceof FloatValue) return new FloatValue(this.value, context);
        return null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function) {
        if(this.isExplicit() && operand.isExplicit()) {
            if(operand instanceof NumericalValue) {
                int weightDiff = this.getWeight() - ((NumericalValue) operand).getWeight();
                if(weightDiff > 0) return operation(operator, ((NumericalValue) operand).coerce(this), pattern, function);
                else if(weightDiff < 0) return this.coerce((NumericalValue) operand).operation(operator, operand, pattern, function);
                else {
                    //We can be certain that if this code is running, then both operands are IntegerValues

                    IntegerValue intOperand = (IntegerValue) operand;

                    switch(operator) {
                        case ADD: return new IntegerValue(this.value + intOperand.value, context);
                        case SUBTRACT: return new IntegerValue(this.value - intOperand.value, context);
                        case MULTIPLY: return new IntegerValue(this.value * intOperand.value, context);
                        case DIVIDE: return new IntegerValue(this.value / intOperand.value, context);//Should probably add a case for division by zero
                        case MODULO: return new IntegerValue(this.value % intOperand.value, context); //Should probably add a case for division by zero
                        case EQUAL: return new BooleanValue(this.value == intOperand.value, context);
                        case LESS_THAN: return new BooleanValue(this.value < intOperand.value, context);
                        case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value <= intOperand.value, context);
                        case GREATER_THAN: return new BooleanValue(this.value > intOperand.value, context);
                        case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value >= intOperand.value, context);
                    }
                    return null;
                }
            } else if(operand instanceof StringValue && operator == Operator.ADD) {
                return new StringValue(String.valueOf(this.value)+((StringValue)operand).getRawValue(), this.context);
            }
        } else if(operand.isExplicit()) {
            Console.debug.println("DOING IMPLICIT OPERATIONS WITH INTEGERS");
            if(operand instanceof NumericalValue) {
                //Deal with floats later
                switch(operator) {
                    case ADD: {
                        String newObjective = context.getAnalyzer().getPrefix() + "_op";
                        function.addCommand("scoreboard players operation @s " + newObjective + " = " + reference.getEntity().toSelector(function) + " " + reference.getObjectiveName());
                        function.addCommand("scoreboard players add @s " + newObjective + " " + ((NumericalValue) operand).getRawValue());
                        return new IntegerValue(new ObjectivePointer(new SelectorReference("@s"), newObjective), context);
                    }
                }
            }
        }
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
    public int getScoreboardValue() {
        return value;
    }

    @Override
    public Integer getRawValue() {
        return this.value;
    }
}
