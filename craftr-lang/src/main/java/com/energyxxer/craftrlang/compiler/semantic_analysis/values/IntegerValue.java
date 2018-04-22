package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.commands.scoreboard.ScoreAdd;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScorePlayersOperation;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.NegatedBooleanReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.ScoreComparisonBooleanReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitInt;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class IntegerValue extends NumericValue {

    public IntegerValue(SemanticContext semanticContext) {
        this(0, semanticContext);
    }

    public IntegerValue(int value, SemanticContext semanticContext) {
        super(semanticContext);
        this.reference = new ExplicitInt(value);
    }

    public IntegerValue(DataReference reference, SemanticContext semanticContext) {
        super(semanticContext);
        this.reference = reference;

        if(reference == null) {
            String debug = "here";
        }
    }

    /**
     * Coerces this <code>IntegerValue</code> to the numeric type of highest weight between itself and the parameter.
     * <br>
     * If <code>other</code> is the same type or lower, <code>this</code> is returned.
     * <br>
     * Otherwise, this value is converted to whatever type <code>other</code> is.
     * */
    @Override
    public NumericValue coerce(NumericValue other) {
        if(other instanceof IntegerValue) return this;
        if(other instanceof FloatValue) return new FloatValue(this.reference, semanticContext);
        return null;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean silent) {
        /* Note to future self:
         *     for incrementing, make sure to change this.value, then return clones of this value.
         */
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {

        if(this.reference instanceof ExplicitInt
                &&
                operand.reference instanceof ExplicitInt) {
            int a = ((ExplicitInt) this.reference).getValue();
            int b = ((ExplicitInt) operand.reference).getValue();

            int explicitResult;

            switch(operator) {
                case ADD: explicitResult = a + b; break;
                case SUBTRACT: explicitResult = a - b; break;
                case MULTIPLY: explicitResult = a * b; break;
                case DIVIDE: {
                    if(b == 0) {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unable to divide by zero", pattern));
                        explicitResult = a;
                    } else explicitResult = a % b;
                    break;
                }
                case MODULO: {
                    if(b == 0) {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unable to divide by zero", pattern));
                        explicitResult = a;
                    } else explicitResult = a % b;
                    break;
                }
                case EQUAL: return new BooleanValue(a == b, semanticContext);
                case NOT_EQUAL: return new BooleanValue(a != b, semanticContext);
                case LESS_THAN: return new BooleanValue(a < b, semanticContext);
                case LESS_THAN_OR_EQUAL: return new BooleanValue(a <= b, semanticContext);
                case GREATER_THAN: return new BooleanValue(a > b, semanticContext);
                case GREATER_THAN_OR_EQUAL: return new BooleanValue(a >= b, semanticContext);
                default: {
                    semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator " + operator.getSymbol() + " is not defined for data type " + getDataType(), pattern));
                    return null;
                }
            }

            return new IntegerValue(new ExplicitInt(explicitResult), semanticContext);
        } else {
            DataReference a = this.reference;
            DataReference b = operand.reference;

            ScoreReference bScore = null;
            LocalizedObjective tempB = null;
            if(b instanceof ScoreReference) bScore = (ScoreReference) b;
            else if(!(b instanceof ExplicitInt)) {
                tempB = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
                tempB.claim();
                bScore = b.toScore(function, new LocalScore(tempB.getObjective(), semanticContext.getScoreHolder(function)), semanticContext);
            }

            LocalizedObjective op = null;

            if(resultReference == null) {
                op = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
                op.claim();
                resultReference = new ScoreReference(new LocalScore(op.getObjective(), semanticContext.getScoreHolder(function)));
            }

            LocalScore score = resultReference.getScore();

            switch(operator) {
                case ADD: {
                    a.toScore(function, score, semanticContext);
                    if(b instanceof ExplicitInt) {
                        function.append(new ScoreAdd(score, ((ExplicitInt) b).getValue()));
                    } else {
                        function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ADD, bScore.getScore()));
                    }
                    if(tempB != null) tempB.dispose();
                    if(op != null) op.dispose();
                    return new IntegerValue(new ScoreReference(score), semanticContext);
                }
                case SUBTRACT: {
                    a.toScore(function, score, semanticContext);
                    if(b instanceof ExplicitInt) {
                        function.append(new ScoreAdd(score, -((ExplicitInt) b).getValue()));
                    } else {
                        function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.SUBTRACT, bScore.getScore()));
                    }
                    if(tempB != null) tempB.dispose();
                    if(op != null) op.dispose();
                    return new IntegerValue(new ScoreReference(score), semanticContext);
                }
                case EQUAL: {
                    return new BooleanValue(new ScoreComparisonBooleanReference(a, ScoreComparison.EQUAL, b), semanticContext);
                }
                case NOT_EQUAL: {
                    return new BooleanValue(new NegatedBooleanReference(new ScoreComparisonBooleanReference(a, ScoreComparison.EQUAL, b)), semanticContext);
                }
                case LESS_THAN: {
                    return new BooleanValue(new ScoreComparisonBooleanReference(a, ScoreComparison.LESS_THAN, b), semanticContext);
                }
                case LESS_THAN_OR_EQUAL: {
                    return new BooleanValue(new ScoreComparisonBooleanReference(a, ScoreComparison.LESS_THAN_EQUAL, b), semanticContext);
                }
                case GREATER_THAN: {
                    return new BooleanValue(new ScoreComparisonBooleanReference(a, ScoreComparison.GREATER_THAN, b), semanticContext);
                }
                case GREATER_THAN_OR_EQUAL: {
                    return new BooleanValue(new ScoreComparisonBooleanReference(a, ScoreComparison.GREATER_THAN_EQUAL, b), semanticContext);
                }
                default: {
                    semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operation not supported for implicit values", pattern));
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
        return "IntegerValue(" + reference + ")";
    }

    @Override
    public Integer getRawValue() {
        return 0;
    }

    @Override
    public IntegerValue clone(Function function) {
        return new IntegerValue(this.reference, semanticContext);
    }
}
