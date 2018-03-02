package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveGroup;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;

public enum VariableType {
    FIELD(m -> m.FIELD), PARAMETER(m -> m.PARAMETER), VARIABLE(m -> m.VARIABLE);

    private final ObjectiveGroupChooser chooser;

    VariableType(ObjectiveGroupChooser chooser) {
        this.chooser = chooser;
    }

    LocalizedObjectiveGroup getGroup(LocalizedObjectiveManager m) {
        return chooser.choose(m);
    }
}
