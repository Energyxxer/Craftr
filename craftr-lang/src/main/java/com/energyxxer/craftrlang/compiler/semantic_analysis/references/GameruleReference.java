package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreScore;
import com.energyxxer.commodore.commands.gamerule.GameruleQueryCommand;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public class GameruleReference implements DataReference {

    private final String gamerule;

    public GameruleReference(String gamerule) {
        this.gamerule = gamerule;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new GameruleQueryCommand(gamerule));
        exec.addModifier(new ExecuteStoreScore(score));

        function.append(exec);

        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        LocalizedObjective obj = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
        obj.claim();
        LocalScore locScr = new LocalScore(obj.getObjective(), entity);
        ScoreReference scoreRef = this.toScore(function, locScr, semanticContext);
        obj.dispose();
        return scoreRef.toNBT(function, entity, path, semanticContext);
    }

    @Override
    public String toString() {
        return "gamerule: " + gamerule;
    }

    @Override
    public BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext) {

        LocalizedObjective locObj;
        ScoreReference thisScoreReference;
        {
            locObj = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
            locObj.claim();
            thisScoreReference = this.toScore(function, new LocalScore(locObj.getObjective(), semanticContext.getScoreHolder()), semanticContext);
        }
        locObj.dispose();

        return thisScoreReference.compare(function, op, other, semanticContext);
    }
}
