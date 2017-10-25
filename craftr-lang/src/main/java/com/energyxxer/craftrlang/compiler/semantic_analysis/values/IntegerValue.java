package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.CompoundInstruction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.ScoreboardCommand;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.ScoreboardOperation;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
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
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        /* Note to future self:
         *     for incrementing, make sure to change this.value, then return clones of this value.
         */
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {

        switch(operator) {
            case ASSIGN: {
                if(operand instanceof NumericalValue && ((NumericalValue) operand).getWeight() <= this.getWeight()) {
                    if(operand instanceof IntegerValue) this.value = ((IntegerValue) operand).value;
                    this.reference = operand.clone(function).getReference();
                    //TODO SOMETHING ABOUT REFERENCES PLEASE.
                    return this.clone(function);
                } else {
                    if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern.getFormattedPath()));
                    return null;
                }
            }
            case ADD_THEN_ASSIGN: {
                if(operand instanceof NumericalValue && ((NumericalValue) operand).getWeight() <= this.getWeight()) {
                    if(this.isExplicit()) {
                        if(operand.isExplicit()) {
                            if(operand instanceof IntegerValue) this.value += ((IntegerValue) operand).value;
                            return this.clone(function);
                        } else {
                            //WHAT TO DO IF THIS IS EXPLICIT BUT THE OTHER THING IS IMPLICIT HELP
                            return null;
                        }
                    } else {
                        if(operand.isExplicit()) {
                            if(operand instanceof IntegerValue) function.addInstruction(new ScoreboardCommand(reference, ScoreboardCommand.ADD, operand));
                            return this.clone(function);
                        } else {
                            if(operand instanceof IntegerValue) function.addInstruction(new ScoreboardOperation(reference, ScoreboardOperation.ADD, operand.getReference()));
                            return this.clone(function);
                        }
                    }
                }
            }
        }

        if(this.isExplicit() && operand.isExplicit()) {
            if(operand instanceof NumericalValue) {
                int weightDiff = this.getWeight() - ((NumericalValue) operand).getWeight();
                if(weightDiff > 0) return operation(operator, ((NumericalValue) operand).coerce(this), pattern, function, fromVariable, silent);
                else if(weightDiff < 0) return this.coerce((NumericalValue) operand).operation(operator, operand, pattern, function, fromVariable, silent);
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
                String newObjective = context.getAnalyzer().getPrefix() + "_op";
                switch(operator) {
                    case ADD: {
                        function.addInstruction(new CompoundInstruction(
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.ASSIGN,
                                        reference
                                ),
                                new ScoreboardCommand(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardCommand.ADD,
                                        operand
                                )
                        ));
                        return new IntegerValue(new ObjectivePointer(new SelectorReference(context), newObjective), context);
                    }
                    case SUBTRACT: {
                        function.addInstruction(new CompoundInstruction(
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.ASSIGN,
                                        reference
                                ),
                                new ScoreboardCommand(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardCommand.REMOVE,
                                        operand
                                )
                        ));
                        return new IntegerValue(new ObjectivePointer(new SelectorReference(context), newObjective), context);
                    }
                    case MULTIPLY: {
                        ObjectivePointer fakePlayerReference = new ObjectivePointer(
                                new SelectorReference(
                                        context.getAnalyzer().getPrefix() + "_math",
                                        context
                                ),
                                context.getAnalyzer().getPrefix() + "_g"
                        );

                        function.addInstruction(new CompoundInstruction(
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.ASSIGN,
                                        reference
                                ),
                                new ScoreboardCommand(
                                        fakePlayerReference,
                                        ScoreboardCommand.SET,
                                        operand
                                ),
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.MULTIPLY,
                                        fakePlayerReference
                                )
                        ));
                        return new IntegerValue(new ObjectivePointer(new SelectorReference(context), newObjective), context);
                    }
                    case DIVIDE: {
                        ObjectivePointer fakePlayerReference = new ObjectivePointer(
                                new SelectorReference(
                                        context.getAnalyzer().getPrefix() + "_math",
                                        context
                                ),
                                context.getAnalyzer().getPrefix() + "_g"
                        );

                        function.addInstruction(new CompoundInstruction(
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.ASSIGN,
                                        reference
                                ),
                                new ScoreboardCommand(
                                        fakePlayerReference,
                                        ScoreboardCommand.SET,
                                        operand
                                ),
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.DIVIDE,
                                        fakePlayerReference
                                )
                        ));
                        return new IntegerValue(new ObjectivePointer(new SelectorReference(context), newObjective), context);
                    }
                    case MODULO: {
                        ObjectivePointer fakePlayerReference = new ObjectivePointer(
                                new SelectorReference(
                                        context.getAnalyzer().getPrefix() + "_math",
                                        context
                                ),
                                context.getAnalyzer().getPrefix() + "_g"
                        );

                        function.addInstruction(new CompoundInstruction(
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.ASSIGN,
                                        reference
                                ),
                                new ScoreboardCommand(
                                        fakePlayerReference,
                                        ScoreboardCommand.SET,
                                        operand
                                ),
                                new ScoreboardOperation(
                                        new ObjectivePointer(new SelectorReference(context), newObjective),
                                        ScoreboardOperation.MODULO,
                                        fakePlayerReference
                                )
                        ));
                        return new IntegerValue(new ObjectivePointer(new SelectorReference(context), newObjective), context);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getScoreboardValue() {
        return (this.isExplicit()) ? value : Integer.MIN_VALUE;
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
    public IntegerValue clone(MCFunction function) {
        if(this.isExplicit()) {
            return new IntegerValue(this.value, context);
        } else {
            ObjectivePointer newReference = new ObjectivePointer(
                    new SelectorReference(context.getAnalyzer().getPrefix() + "_CLONE",context),
                    context.getAnalyzer().getPrefix() + "_g"
            );

            function.addInstruction(
                    new ScoreboardOperation(
                            newReference,
                            ScoreboardOperation.ASSIGN,
                            this.reference
                    )
            );
            //TODO Change this instruction to a compound instruction to remotely add the cloning instruction when the value is used next.
            return new IntegerValue(newReference, context);
        }
    }
}
