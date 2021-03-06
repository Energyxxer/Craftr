package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.data.DataGetCommand;
import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreEntity;
import com.energyxxer.commodore.commands.execute.ExecuteStoreScore;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.NumericNBTType;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public class NBTReference implements DataReference {
    private Entity entity;
    private NBTPath path;

    public NBTReference(Entity entity, NBTPath path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public ScoreReference toScore(FunctionSection section, LocalScore score, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new DataGetCommand(this.entity.limitToOne(), this.path));
        exec.addModifier(new ExecuteStoreScore(score));

        section.append(exec);

        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(FunctionSection section, Entity entity, NBTPath path, SemanticContext semanticContext) {
        if(!this.entity.equals(entity) || !this.path.equals(path)) {
            ExecuteCommand exec = new ExecuteCommand(new DataGetCommand(this.entity.limitToOne(), this.path));
            exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.DOUBLE)); //TODO: Dynamically choose data type

            section.append(exec);

            return new NBTReference(entity, path);
        } else return this;
    }

    @Override
    public BooleanResolution compare(FunctionSection section, ScoreComparison op, DataReference other, SemanticContext semanticContext) {

        LocalizedObjective locObj;
        ScoreReference thisScoreReference;
        {
            locObj = semanticContext.getLocalizedObjectiveManager().OPERATION.create();
            locObj.claim();
            thisScoreReference = this.toScore(section, new LocalScore(locObj.getObjective(), semanticContext.getScoreHolder(section)), semanticContext);
        }
        locObj.dispose();

        return thisScoreReference.compare(section, op, other, semanticContext);
    }

    public Entity getEntity() {
        return entity;
    }

    public NBTPath getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "nbt: " + entity + ":" + path;
    }
}
