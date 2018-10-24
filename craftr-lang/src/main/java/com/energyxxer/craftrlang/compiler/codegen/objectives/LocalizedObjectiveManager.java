package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.craftrlang.compiler.CraftrCommandModule;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class LocalizedObjectiveManager {
    private final CraftrCommandModule module;
    private final SemanticContext semanticContext;

    public final LocalizedObjectiveGroup GENERIC;
    public final LocalizedObjectiveGroup PARAMETER;
    public final LocalizedObjectiveGroup OPERATION;
    public final LocalizedObjectiveGroup VARIABLE;
    public final LocalizedObjectiveGroup FIELD;

    public LocalizedObjectiveManager(CraftrCommandModule module, SemanticContext semanticContext) {
        this.module = module;
        this.semanticContext = semanticContext;

        GENERIC = new LocalizedObjectiveGroup(this, "g");
        PARAMETER = new LocalizedObjectiveGroup(this, "p");
        OPERATION = new LocalizedObjectiveGroup(this, "op");
        VARIABLE = new LocalizedObjectiveGroup(this, "v");
        FIELD = new LocalizedObjectiveGroup(this, "f", true);
    }

    public SemanticContext getSemanticContext() {
        return semanticContext;
    }

    public CraftrCommandModule getModule() {
        return module;
    }
}
