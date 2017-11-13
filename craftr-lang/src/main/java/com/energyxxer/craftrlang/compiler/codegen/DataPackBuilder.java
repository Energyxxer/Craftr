package com.energyxxer.craftrlang.compiler.codegen;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.codegen.functions.MCFunctionManager;
import com.energyxxer.craftrlang.compiler.codegen.objectives.ObjectiveManager;
import com.energyxxer.craftrlang.compiler.codegen.players.ScoreHolderManager;

public class DataPackBuilder {
    private final Compiler compiler;

    private MCFunctionManager functionManager;
    private ObjectiveManager objectiveManager;
    private ScoreHolderManager scoreHolderManager;

    public DataPackBuilder(Compiler compiler) {
        this.compiler = compiler;

        this.functionManager = new MCFunctionManager(compiler);
        this.objectiveManager = new ObjectiveManager(compiler);
        this.scoreHolderManager = new ScoreHolderManager(this);
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public MCFunctionManager getFunctionManager() {
        return functionManager;
    }

    public ObjectiveManager getObjectiveManager() {
        return objectiveManager;
    }

    public ScoreHolderManager getScoreHolderManager() {
        return scoreHolderManager;
    }
}
