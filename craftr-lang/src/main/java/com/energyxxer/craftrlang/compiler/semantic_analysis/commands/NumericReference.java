package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.CompoundInstruction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.ScoreboardOperation;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute.ExecuteAs;
import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute.ExecuteCommand;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;

public class NumericReference extends EntityReference {
    private ObjectivePointer idPointer;

    public NumericReference(ObjectivePointer idPointer, Context context) {
        super(context);
        this.idPointer = idPointer;
    }

    @Override
    public Instruction getInstruction() {
        return new ExecuteCommand(new CompoundInstruction(
                new ScoreboardOperation(
                        new ObjectivePointer(new SelectorReference(context), context.getAnalyzer().getPrefix() + "_o_id"),
                        ScoreboardOperation.ASSIGN,
                        new ObjectivePointer(new SelectorReference(context), context.getAnalyzer().getPrefix() + "_id")),
                new ScoreboardOperation(
                        new ObjectivePointer(new SelectorReference(context), context.getAnalyzer().getPrefix() + "_o_id"),
                        ScoreboardOperation.SUBTRACT,
                        idPointer)
        ), new ExecuteAs(new SelectorReference("@e", context)));
    }

    @Override
    public String toSelector() {
        return "@e[score_craftr_o_id=0]";
    }

    @Override
    public String toString() {
        return "@<" + idPointer + ">";
    }
}
