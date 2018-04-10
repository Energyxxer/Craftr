package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution.State.TRUE;

public interface BooleanReference extends DataReference {
    BooleanResolution resolveBoolean(Function function, SemanticContext semanticContext, boolean negated);

    default BooleanReference negate() {
        if(this instanceof NegatedBooleanReference) return ((NegatedBooleanReference) this).getReference();
        else return new NegatedBooleanReference(this);
    }

    @Override
    default ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        BooleanResolution resolution = resolveBoolean(function, semanticContext, false);
        if(resolution.getState() == BooleanResolution.State.IMPLICIT) {
            function.append(new ScoreSet(score, 0));
            function.append(new ExecuteCommand(new ScoreSet(score, 1), resolution.getModifiers()));
        } else {
            function.append(new ScoreSet(score, resolution.getState() == TRUE ? 1 : 0));
        }
        return new ScoreReference(score);
    }

    @Override
    default NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        return null;
    }

    @Override
    default BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        return null;
    }
}
