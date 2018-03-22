package com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans;

import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class NegatedBooleanReference implements BooleanReference {
    private BooleanReference reference;

    public NegatedBooleanReference(BooleanReference reference) {
        this.reference = reference;
    }

    public BooleanReference getReference() {
        return reference;
    }

    @Override
    public BooleanResolution resolveBoolean(Function function, SemanticContext semanticContext, boolean negated) {
        return reference.resolveBoolean(function, semanticContext, !negated);
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        throw new NotImplementedException();
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        throw new NotImplementedException();
    }
}
