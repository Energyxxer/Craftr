package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveGroup;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;

public interface ObjectiveGroupChooser {
    LocalizedObjectiveGroup choose(LocalizedObjectiveManager m);
}
