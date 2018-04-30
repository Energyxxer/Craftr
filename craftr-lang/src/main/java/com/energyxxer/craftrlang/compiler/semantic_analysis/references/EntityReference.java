package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreEntity;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreGet;
import com.energyxxer.commodore.commands.scoreboard.ScorePlayersOperation;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.NumericNBTType;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public class EntityReference implements DataReference {

    private CraftrEntity entity;

    public EntityReference(CraftrEntity entity) {
        this.entity = entity;
    }

    public EntityReference(Unit unit, ScoreReference score) {
        this.entity = new CraftrEntity(unit, score);
    }

    private LocalScore getId(SemanticContext semanticContext) {
        return new LocalScore(semanticContext.getModule().glObjMgr.id, entity);
    }

    @Override
    public ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext) {
        section.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ASSIGN, getId(semanticContext)));
        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new ScoreGet(getId(semanticContext)));
        exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.INT));
        return new NBTReference(entity, path);
    }

    @Override
    public BooleanResolution compare(FunctionSection section, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        return null;
    }

    public CraftrEntity getEntity() {
        return entity;
    }
}
