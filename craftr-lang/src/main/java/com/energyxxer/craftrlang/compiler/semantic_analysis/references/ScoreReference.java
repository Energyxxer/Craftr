package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.execute.*;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreGet;
import com.energyxxer.commodore.commands.scoreboard.ScorePlayersOperation;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.NumericNBTType;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.selector.SelectorNumberArgument;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitInt;

public class ScoreReference implements DataReference {
    private LocalScore score;

    public ScoreReference(LocalScore score) {
        this.score = score;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        if(!score.equals(this.score)) {
            function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ASSIGN, this.score));
            return new ScoreReference(score);
        }
        return this;
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new ScoreGet(this.score));
        exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.DOUBLE));
        return new NBTReference(entity, path);
    }

    @Override
    public BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        if(other instanceof ExplicitInt) {
            return new BooleanResolution(new ExecuteConditionScoreMatch(ExecuteCondition.ConditionType.IF, score, new SelectorNumberArgument<>(((ExplicitInt) other).getValue())));
        }

        LocalizedObjective locObj = null;
        ScoreReference otherScoreReference;

        if(other instanceof ScoreReference) {
            otherScoreReference = (ScoreReference) other;
        } else {
            locObj = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
            locObj.claim();

            otherScoreReference = other.toScore(function, new LocalScore(locObj.getObjective(), semanticContext.getScoreHolder(function)), semanticContext);
        }

        if(locObj != null) locObj.dispose();

        return new BooleanResolution(new ExecuteConditionScoreComparison(ExecuteCondition.ConditionType.IF, score, op, otherScoreReference.score));
    }

    public LocalScore getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "score: " + score;
    }
}
