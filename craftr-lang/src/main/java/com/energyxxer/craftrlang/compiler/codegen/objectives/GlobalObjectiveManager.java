package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.commodore.score.FakePlayer;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;

public class GlobalObjectiveManager {
    private final CraftrCommandModule module;
    private final Objective global;

    public final LocalScore RETURN;

    public GlobalObjectiveManager(CraftrCommandModule module) {
        this.module = module;

        this.global = module.getObjectiveManager().create("global", "dummy", "Global Data", false);

        this.RETURN = new LocalScore(global, new FakePlayer("RETURN"));
    }

    public Objective getGlobal() {
        return global;
    }
}
