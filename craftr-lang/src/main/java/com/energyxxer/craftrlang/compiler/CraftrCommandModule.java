package com.energyxxer.craftrlang.compiler;

import com.energyxxer.commodore.module.CommandModule;
import com.energyxxer.commodore.module.Namespace;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class CraftrCommandModule extends CommandModule {

    public final Namespace projectNS;

    public CraftrCommandModule(String name, String prefix) {
        this(name, null, prefix);
    }

    public CraftrCommandModule(String name, String description, String prefix) {
        super(name, description, prefix);

        this.projectNS = this.getNamespace(prefix);
    }

    public LocalizedObjectiveManager createLocalizedObjectiveManager(SemanticContext semanticContext) {
        return new LocalizedObjectiveManager(this, semanticContext);
    }
}
