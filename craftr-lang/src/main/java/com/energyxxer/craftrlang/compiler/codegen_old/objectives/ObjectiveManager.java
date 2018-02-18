package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.codegen.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.ScoreboardDefinition;

import java.util.HashMap;

public class ObjectiveManager {
    private final Compiler compiler;
    private HashMap<String, Objective> allObjectives = new HashMap<>();

    //
    // TODO: Hey, you know what you're doing with these stupid objective builders?
    // TODO: That's really dumb. Make a ScoreHolderManager with each of these objective groups in them to make objective use
    // TODO: on a player-by-player basis instead of a global thing.
    // TODO: We don't want 500 objectives when each entity could be reusing the same 5
    //
    // Also good morning :)
    //

    //public final ObjectiveGroup OPERATION = new ObjectiveGroup(this, "op", "Operation");
    //public final ObjectiveGroup ARGUMENT = new ObjectiveGroup(this, "a", "Argument");
    //public final ObjectiveGroup GENERIC = new ObjectiveGroup(this, "g", "Generic");

    public ObjectiveManager(Compiler compiler) {
        this.compiler = compiler;
    }

    public Objective createObjective(String name, String displayName) {
        return createObjective(name, displayName, null);
    }

    public Objective createObjective(String name, String displayName, String type) {

        Objective cached = allObjectives.get(name);
        if(cached != null) return cached;

        Objective newObjective = new Objective(name, displayName, type);
        this.allObjectives.put(name, newObjective);
        return newObjective;
    }

    public void buildDeclarationFunction() {
        MCFunction function = compiler.getDataPackBuilder().getFunctionManager().createFunction("setup");
        allObjectives.values().forEach(o -> function.addInstruction(new ScoreboardDefinition(o)));
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
