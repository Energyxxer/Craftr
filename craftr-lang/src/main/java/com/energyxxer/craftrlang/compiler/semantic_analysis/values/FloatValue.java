package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitFloat;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class FloatValue extends NumericValue {

    public FloatValue(float value, SemanticContext semanticContext) {
        super(semanticContext);
        this.reference = new ExplicitFloat(value);
    }

    public FloatValue(DataReference reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    @Override
    public NumericValue coerce(NumericValue value) {
        return this;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        /*
        if(operator == Operator.ASSIGN) {
            if(operand instanceof NumericValue && ((NumericValue) operand).getWeight()<=this.getWeight()) {
                if(operand instanceof IntegerValue) this.value = ((IntegerValue) operand).getRawValue().floatValue();
                if(operand instanceof FloatValue) this.value = ((FloatValue) operand).value;
                this.reference = operand.clone(function).getReference();
                return this.clone(function);
            } else {
                if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern));
                return null;
            }
        }

        if(operand instanceof NumericValue) {
            int weightDiff = this.getWeight() - ((NumericValue) operand).getWeight();
            if(weightDiff > 0) return runOperation(operator, ((NumericValue) operand).coerce(this), pattern, function, fromVariable, silent);
            else if(weightDiff < 0) return this.coerce((NumericValue) operand).runOperation(operator, operand, pattern, function, fromVariable, silent);
            else {
                //We can be certain that if this code is running, then both operands are FloatValues

                FloatValue floatOperand = (FloatValue) operand;

                switch(operator) {
                    case ADD: return new FloatValue(this.value + floatOperand.value, semanticContext);
                    case SUBTRACT: return new FloatValue(this.value - floatOperand.value, semanticContext);
                    case MULTIPLY: return new FloatValue(this.value * floatOperand.value, semanticContext);
                    case DIVIDE: return new FloatValue(this.value / floatOperand.value, semanticContext);//Should probably add a case for division by zero
                    case MODULO: return new FloatValue(this.value % floatOperand.value, semanticContext); //Should probably add a case for division by zero
                    case EQUAL: return new BooleanValue(this.value == floatOperand.value, semanticContext);
                    case LESS_THAN: return new BooleanValue(this.value < floatOperand.value, semanticContext);
                    case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value <= floatOperand.value, semanticContext);
                    case GREATER_THAN: return new BooleanValue(this.value > floatOperand.value, semanticContext);
                    case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value >= floatOperand.value, semanticContext);
                }
                return null;
            }
        } else if(operand instanceof StringValue && operator == Operator.ADD) {
            return new StringValue(String.valueOf(this.value)+((StringValue)operand).getRawValue(), this.semanticContext);
        }*/
        return null;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public DataType getDataType() {
        return DataType.FLOAT;
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
        return "FloatValue(" + reference + ")";
    }

    @Override
    public Float getRawValue() {
        return 0.0f;
    }

    @Override
    public FloatValue clone(Function function) {
        return new FloatValue(reference, semanticContext);
    }
}
