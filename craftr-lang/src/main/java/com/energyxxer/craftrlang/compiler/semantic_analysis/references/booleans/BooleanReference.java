package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreSet;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution.State.TRUE;

public interface BooleanReference extends DataReference {
    BooleanResolution resolveBoolean(FunctionSection section, SemanticContext semanticContext, boolean negated);

    default BooleanReference negate() {
        if(this instanceof NegatedBooleanReference) return ((NegatedBooleanReference) this).getReference();
        else return new NegatedBooleanReference(this);
    }

    @Override
    default ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext) {
        BooleanResolution resolution = resolveBoolean(section, semanticContext, false);
        if(resolution.getState() == BooleanResolution.State.IMPLICIT) {
            section.append(new ScoreSet(score, 0));
            section.append(new ExecuteCommand(new ScoreSet(score, 1), resolution.getModifiers()));
        } else {
            section.append(new ScoreSet(score, resolution.getState() == TRUE ? 1 : 0));
        }
        return new ScoreReference(score);
    }

    @Override
    default NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext) {
        return null;
    }

    @Override
    default BooleanResolution compare(FunctionSection section, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        return null;
    }
}
