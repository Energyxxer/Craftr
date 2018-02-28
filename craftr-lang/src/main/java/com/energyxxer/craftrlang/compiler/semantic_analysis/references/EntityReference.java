package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreEntity;
import com.energyxxer.commodore.commands.scoreboard.ScoreGet;
import com.energyxxer.commodore.commands.scoreboard.ScorePlayersOperation;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.NumericNBTType;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.entities.CraftrEntity;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class EntityReference implements DataReference {

    private CraftrEntity entity;

    public EntityReference(CraftrEntity entity) {
        this.entity = entity;
    }

    private LocalScore getId(SemanticContext semanticContext) {
        return new LocalScore(semanticContext.getModule().glObjMgr.getGlobal(), entity);
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ASSIGN, getId(semanticContext)));
        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new ScoreGet(getId(semanticContext)));
        exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.INT));
        return new NBTReference(entity, path);
    }
}
