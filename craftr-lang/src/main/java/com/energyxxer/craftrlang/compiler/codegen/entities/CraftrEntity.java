package com.energyxxer.craftrlang.compiler.codegen.entities;

import com.energyxxer.commodore.commands.execute.ExecuteAsEntity;
import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteCondition;
import com.energyxxer.commodore.commands.execute.ExecuteConditionScoreComparison;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.tag.TagCommand;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.entity.GenericEntity;
import com.energyxxer.commodore.inspection.EntityResolution;
import com.energyxxer.commodore.inspection.ExecutionContext;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.MacroScore;
import com.energyxxer.commodore.score.MacroScoreHolder;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.commodore.score.access.ScoreboardAccess;
import com.energyxxer.commodore.selector.LimitArgument;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.selector.TagArgument;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CraftrEntity implements Entity {

    private Unit unit;
    private Selector selector;
    private ScoreReference scoreReference;
    private Collection<ScoreboardAccess> scoreboardAccesses;
    private final ArrayList<MacroScoreHolder> macroHolders = new ArrayList<>();

    //Possible structure:
    //  A list of properties like "close to entity", "in value x", "present in return value..." etc.

    //Create entity from selector
    public CraftrEntity(Unit unit, Selector selector) {
        this.unit = unit;
        this.selector = selector;
        addMacroHolder(new MacroScoreHolder("CE:" + unit.getName() + "#" + this.hashCode()));
        addMacroHolder(new MacroScoreHolder("CE:INSTANCEOF:" + unit.getFullyQualifiedName()));
    }

    //Create entity from ID stored in scoreboard
    public CraftrEntity(Unit unit, ScoreReference score) {
        this.unit = unit;
        this.selector = null;
        this.scoreReference = score;
        addMacroHolder(new MacroScoreHolder("CE:ID:" + score.toString()));
        addMacroHolder(new MacroScoreHolder("CE:" + unit.getName() + "#" + this.hashCode()));
        addMacroHolder(new MacroScoreHolder("CE:INSTANCEOF:" + unit.getFullyQualifiedName()));
    }

    private CraftrEntity(CraftrEntity that) {
        this.unit = that.unit;
        this.selector = that.selector.clone();
        this.macroHolders.addAll(that.macroHolders);
    }

    @Override
    public int getLimit() {
        return selector.getLimit();
    }

    @Override
    public EntityResolution resolveFor(ExecutionContext context) {
        if(context.getFinalSender() == this) return new EntityResolution(this, new Selector(Selector.BaseSelector.SENDER));
        if(selector != null) return new EntityResolution(this, selector);
        else {
            Entity allEntities = new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES));
            ExecuteCommand exec = new ExecuteCommand(new TagCommand(TagCommand.Action.ADD, allEntities, "it"));
            exec.addModifier(new ExecuteAsEntity(allEntities));
            exec.addModifier(new ExecuteConditionScoreComparison(ExecuteCondition.ConditionType.IF, new LocalScore(unit.getModule().glObjMgr.id, allEntities), ScoreComparison.EQUAL, this.scoreReference.getScore()));

            return new EntityResolution(this, new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument("it")));
        }
    }

    @Override
    public String toString() {
        return selector.toString();
    }

    @Override
    public CraftrEntity clone() {
        return new CraftrEntity(this);
    }

    @Override
    public void addMacroHolder(MacroScoreHolder macro) {
        this.macroHolders.add(macro);
        scoreboardAccesses = null;
    }

    public void addMacroHolders(MacroScoreHolder... macros) {
        this.addMacroHolders(Arrays.asList(macros));
    }

    public void addMacroHolders(Collection<MacroScoreHolder> macros) {
        macros.forEach(this::addMacroHolder);
    }

    @Override
    public Collection<ScoreboardAccess> getScoreboardAccesses() {
        if(scoreboardAccesses == null) createScoreboardAccesses();
        return scoreboardAccesses;
    }

    private void createScoreboardAccesses() {
        if(selector != null) {
            ArrayList<MacroScore> scores = new ArrayList<>();
            for(MacroScoreHolder holder : getMacroHolders()) {
                for(Objective objective : selector.getObjectivesRead()) {
                    scores.add(new MacroScore(holder, objective));
                }
            }
            if(scores.size() == 0)
                scoreboardAccesses = Collections.emptyList();
            else
                scoreboardAccesses = Collections.singletonList(new ScoreboardAccess(scores, ScoreboardAccess.AccessType.READ));
        } else scoreboardAccesses = Collections.emptyList();
    }

    @Override
    public Collection<MacroScoreHolder> getMacroHolders() {
        return macroHolders;
    }

    @Override
    public boolean isPlayer() {
        return selector.isPlayer();
    }

    @Override
    public CraftrEntity limitToOne() {
        Selector newSelector = selector.clone();
        newSelector.addArgument(new LimitArgument(1));
        CraftrEntity clone = new CraftrEntity(unit, newSelector);
        clone.addMacroHolders(macroHolders);
        return clone;
    }
}
