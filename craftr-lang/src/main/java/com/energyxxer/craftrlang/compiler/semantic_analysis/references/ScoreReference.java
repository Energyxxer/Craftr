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

public class ScoreReference implements DataReference {
    private LocalScore score;

    public ScoreReference(LocalScore score) {
        this.score = score;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score) {
        if(!score.equals(this.score)) {
            function.append(new ScorePlayersOperation(score, ScorePlayersOperation.Operation.ASSIGN, this.score));
            return new ScoreReference(score);
        }
        return this;
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path) {
        ExecuteCommand exec = new ExecuteCommand(new ScoreGet(this.score));
        exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.DOUBLE));
        return new NBTReference(entity, path);
    }
}
