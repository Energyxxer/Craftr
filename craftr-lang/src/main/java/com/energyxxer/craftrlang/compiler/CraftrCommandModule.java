package com.energyxxer.craftrlang.compiler;

import com.energyxxer.commodore.module.CommandModule;
import com.energyxxer.commodore.module.Namespace;
import com.energyxxer.craftrlang.compiler.codegen.CraftrObjectiveManager;

public class CraftrCommandModule extends CommandModule {

    public final Namespace projectNS;
    private CraftrObjectiveManager craftrObjectiveManager;

    public CraftrCommandModule(String name, String prefix) {
        this(name, null, prefix);
    }

    public CraftrCommandModule(String name, String description, String prefix) {
        super(name, description, prefix);

        this.projectNS = this.getNamespace(prefix);
    }

    public CraftrObjectiveManager getCraftrObjectiveManager() {
        return craftrObjectiveManager;
    }
}
