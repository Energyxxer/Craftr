package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.ScoreboardOperation;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.ResolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class StringValue extends Value {

    private String value = null;

    public StringValue(String value, Context context) {
        super(context);
        this.value = value;
    }

    public StringValue(UnresolvedObjectiveReference reference, Context context) {
        super(reference, context);
    }

    @Override
    public DataType getDataType() {
        return DataType.STRING;
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
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getRawValue() {
        return this.value;
    }

    @Override
    public StringValue clone(MCFunction function) {
        if(this.isExplicit()) {
            return new StringValue(this.value, context);
        } else {
            ResolvedObjectiveReference newReference = context.resolve(context.getAnalyzer().getCompiler().getDataPackBuilder().getPlayerManager().CLONE.GENERIC.get());

            function.addInstruction(
                    new ScoreboardOperation(
                            newReference,
                            ScoreboardOperation.ASSIGN,
                            context.resolve(reference)
                    )
            );
            return new StringValue(newReference.getUnresolvedObjectiveReference(), context);
        }
    }
}
