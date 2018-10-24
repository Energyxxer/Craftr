package com.energyxxer.craftrlang.compiler.codegen.functions;

import com.energyxxer.commodore.commands.execute.ExecuteAsEntity;
import com.energyxxer.commodore.commands.execute.ExecuteCondition;
import com.energyxxer.commodore.commands.execute.ExecuteConditionScoreComparison;
import com.energyxxer.commodore.commands.execute.ExecuteModifier;
import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.commands.tag.TagCommand;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.entity.GenericEntity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionHeaderComment;
import com.energyxxer.commodore.inspection.EntityResolution;
import com.energyxxer.commodore.inspection.ExecutionContext;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.MacroScore;
import com.energyxxer.commodore.score.MacroScoreHolder;
import com.energyxxer.commodore.score.access.ScoreboardAccess;
import com.energyxxer.commodore.selector.LimitArgument;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.selector.TagArgument;
import com.energyxxer.craftrlang.compiler.CraftrCommandModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class HelperFunctionManager {
    private final CraftrCommandModule module;
    public final Function RESTORE_SCOPE_TAGS;

    public HelperFunctionManager(CraftrCommandModule module) {
        this.module = module;

        {
            this.RESTORE_SCOPE_TAGS = module.projectNS.getFunctionManager().create("craftr/restore_scope", true, module.glObjMgr.SCOPE_NOW);
            RESTORE_SCOPE_TAGS.append(new FunctionHeaderComment("Remove all the currently active scope tags, and reassign",
                    "them based on the scope objectives of the executing entity's"));

            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.REMOVE, new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES, new TagArgument("scope.now"), new LimitArgument(1))), "scope.now"));
            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.REMOVE, module.glObjMgr.ENTITY_THIS, "entity.this"));
            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.REMOVE, module.glObjMgr.METHOD_NOW, "method.now"));

            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.ADD, new GenericEntity(new Selector(Selector.BaseSelector.SENDER)), "scope.now"));
            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.ADD, new EntityFromScore(module, new LocalScore(module.glObjMgr.parentEntity, module.glObjMgr.SCOPE_NOW)), "parent.entity"));
            RESTORE_SCOPE_TAGS.append(new TagCommand(TagCommand.Action.ADD, new EntityFromScore(module, new LocalScore(module.glObjMgr.parentMethod, module.glObjMgr.SCOPE_NOW)), "parent.method"));
        }
    }

    class EntityFromScore implements Entity {
        private final CraftrCommandModule module;
        private LocalScore score;

        public EntityFromScore(CraftrCommandModule module, LocalScore score) {
            this.module = module;
            this.score = score;
        }

        @Override
        public EntityResolution resolveFor(ExecutionContext executionContext) {
            ArrayList<ExecuteModifier> modifiers = new ArrayList<>();
            modifiers.add(new ExecuteAsEntity(new GenericEntity(new Selector(Selector.BaseSelector.ALL_ENTITIES))));
            modifiers.add(new ExecuteConditionScoreComparison(ExecuteCondition.ConditionType.IF, new LocalScore(module.glObjMgr.id, new GenericEntity(new Selector(Selector.BaseSelector.SENDER))), ScoreComparison.EQUAL, score));
            return new EntityResolution(this, new Selector(Selector.BaseSelector.SENDER), modifiers);
        }

        @Override
        public void addMacroHolder(MacroScoreHolder macroScoreHolder) {

        }

        @Override
        public Collection<ScoreboardAccess> getScoreboardAccesses() {
            return Arrays.asList(new ScoreboardAccess(Collections.singleton(new MacroScore(null, module.glObjMgr.id)), ScoreboardAccess.AccessType.READ),
                    new ScoreboardAccess(score.getMacroScores(), ScoreboardAccess.AccessType.READ));
        }

        @Override
        public int getLimit() {
            return 1;
        }

        @Override
        public Entity limitToOne() {
            return this;
        }

        @Override
        public Entity clone() {
            return new EntityFromScore(module, score);
        }

        @Override
        public boolean isPlayer() {
            return false;
        }

        @Override
        public Collection<MacroScoreHolder> getMacroHolders() {
            return null;
        }
    }
}