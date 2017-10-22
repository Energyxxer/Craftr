package com.energyxxer.craftrlang.compiler.semantic_analysis.commands;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;

public class NumericReference implements EntityReference {
    private ObjectivePointer idPointer;

    public NumericReference(ObjectivePointer idPointer) {
        this.idPointer = idPointer;
    }

    @Override
    public String toSelector(MCFunction function) {
        String sel = idPointer.getEntity().toSelector(function);
        function.addCommand("execute as @e then scoreboard players operation @s craftr_o_id = @s craftr_id");
        function.addCommand("execute as @e then scoreboard players operation @s craftr_o_id -= " + sel + " " + idPointer.getObjectiveName());
        return "@e[score_craftr_o_id=0]";
    }

    @Override
    public String toString() {
        return "@<" + idPointer + ">";
    }
}
