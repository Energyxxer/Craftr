package com.energyxxer.craftrlang.compiler.codegen.entities;

import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.inspection.EntityResolution;
import com.energyxxer.commodore.inspection.ExecutionContext;
import com.energyxxer.commodore.score.MacroScore;
import com.energyxxer.commodore.score.MacroScoreHolder;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.commodore.score.access.ScoreboardAccess;
import com.energyxxer.commodore.selector.LimitArgument;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CraftrEntity implements Entity {

    private Unit unit;
    private final Selector selector;
    private Collection<ScoreboardAccess> scoreboardAccesses;
    private final ArrayList<MacroScoreHolder> macroHolders = new ArrayList<>();

    //Possible structure:
    //  A list of properties like "close to entity", "in value x", "present in return value..." etc.

    public CraftrEntity(Unit unit, Selector selector) {
        this.unit = unit;
        this.selector = selector;
        addMacroHolder(new MacroScoreHolder("GE#" + this.hashCode()));
    }

    @Override
    public int getLimit() {
        return selector.getLimit();
    }

    @Override
    public EntityResolution resolveFor(ExecutionContext context) {
        if(context.getFinalSender() == this) return new EntityResolution(this, new Selector(Selector.BaseSelector.SENDER));
        return new EntityResolution(this, selector);
    }

    @Override
    public String toString() {
        return selector.toString();
    }

    @Override
    public CraftrEntity clone() {
        CraftrEntity copy = new CraftrEntity(unit, selector.clone());
        copy.addMacroHolders(macroHolders);
        return copy;
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
    }

    @Override
    public Collection<MacroScoreHolder> getMacroHolders() {
        return macroHolders;
    }

    @Override
    public boolean isPlayer() {
        return selector.isPlayer();
    }

    public CraftrEntity limitToOne() {
        Selector newSelector = selector.clone();
        newSelector.addArgument(new LimitArgument(1));
        CraftrEntity clone = new CraftrEntity(unit, newSelector);
        clone.addMacroHolders(macroHolders);
        return clone;
    }

    //TODO this
    // Good morning c:
}
