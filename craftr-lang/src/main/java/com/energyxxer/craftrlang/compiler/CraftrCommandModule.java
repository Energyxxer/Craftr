package com.energyxxer.craftrlang.compiler;

import com.energyxxer.commodore.module.CommandModule;
import com.energyxxer.commodore.module.Namespace;
import com.energyxxer.commodore.module.options.UnusedCommandPolicy;
import com.energyxxer.commodore.tags.FunctionTag;
import com.energyxxer.craftrlang.compiler.codegen.objectives.GlobalObjectiveManager;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class CraftrCommandModule extends CommandModule {

    public final Namespace projectNS;

    public final GlobalObjectiveManager glObjMgr;

    public final FunctionTag tickTag;
    public final FunctionTag loadTag;

    public CraftrCommandModule(String name, String prefix) {
        this(name, "Compiled data pack from Craftr project '" + name + "'", prefix);
    }

    public CraftrCommandModule(String name, String description, String prefix) {
        super(name, description, prefix);
        this.projectNS = this.createNamespace(prefix);
        this.objMgr.setPrefixEnabled(true);
        this.glObjMgr = new GlobalObjectiveManager(this);

        this.tickTag = minecraft.getTagManager().functionTags.create("tick");
        this.loadTag = minecraft.getTagManager().functionTags.create("load");

        this.optMgr.UNUSED_COMMAND_POLICY.setValue(UnusedCommandPolicy.COMMENT_OUT);
        this.optMgr.EXPORT_ACCESS_LOGS.setValue(true);
    }

    public LocalizedObjectiveManager createLocalizedObjectiveManager(SemanticContext semanticContext) {
        return new LocalizedObjectiveManager(this, semanticContext);
    }

    public GlobalObjectiveManager getGlobalObjectiveManager() {
        return glObjMgr;
    }
}
