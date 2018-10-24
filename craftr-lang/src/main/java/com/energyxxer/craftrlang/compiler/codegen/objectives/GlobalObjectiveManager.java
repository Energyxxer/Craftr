package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.entity.GenericEntity;
import com.energyxxer.commodore.score.FakePlayer;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.commodore.selector.LimitArgument;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.selector.TagArgument;
import com.energyxxer.commodore.textcomponents.StringTextComponent;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;

public class GlobalObjectiveManager {
    private final CraftrCommandModule module;
    public final Objective global;
    public final Objective id;

    public final Objective parentScope;
    public final Objective parentEntity;
    public final Objective parentMethod;

    public final LocalScore RETURN;

    public final Entity SCOPE_NOW;
    public final Entity ENTITY_THIS;
    public final Entity METHOD_NOW;

    public GlobalObjectiveManager(CraftrCommandModule module) {
        this.module = module;

        this.global = module.getObjectiveManager().create("global", "dummy", new StringTextComponent("Global Data"), true);
        this.id = module.getObjectiveManager().create("id", "dummy", new StringTextComponent("Entity ID"), true);

        this.parentScope = module.getObjectiveManager().create("parent_scope", "dummy", new StringTextComponent("Parent Scope"), true);
        this.parentEntity = module.getObjectiveManager().create("parent_entity", "dummy", new StringTextComponent("Parent Entity"), true);
        this.parentMethod = module.getObjectiveManager().create("parent_method", "dummy", new StringTextComponent("Parent Method"), true);

        this.RETURN = new LocalScore(global, new FakePlayer("RETURN"));

        this.SCOPE_NOW = new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument("scope.now"), new LimitArgument(1)));
        this.ENTITY_THIS = new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument("entity.this"), new LimitArgument(1)));
        this.METHOD_NOW = new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument("method.now"), new LimitArgument(1)));
    }
}
