package com.energyxxer.craftrlang.compiler.semantic_analysis.natives;

import com.energyxxer.commodore.commands.data.DataMergeCommand;
import com.energyxxer.commodore.commands.effect.EffectClearCommand;
import com.energyxxer.commodore.commands.effect.EffectGiveCommand;
import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreScore;
import com.energyxxer.commodore.commands.kill.KillCommand;
import com.energyxxer.commodore.commands.teleport.TeleportCommand;
import com.energyxxer.commodore.commands.teleport.destination.BlockDestination;
import com.energyxxer.commodore.commands.tellraw.TellrawCommand;
import com.energyxxer.commodore.commands.time.TimeQueryCommand;
import com.energyxxer.commodore.coordinates.CoordinateSet;
import com.energyxxer.commodore.effect.StatusEffect;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.entity.GenericEntity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.*;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.selector.Selector;
import com.energyxxer.commodore.textcomponents.StringTextComponent;
import com.energyxxer.commodore.textcomponents.TextComponent;
import com.energyxxer.commodore.types.EffectType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.NBTReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitBoolean;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitString;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.ActualParameter;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.IntegerValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.HashMap;
import java.util.List;

public class NativeMethods {
    private static HashMap<String, MethodExecutor> methods = new HashMap<>();

    static {
        // Math
        methods.put("craftr.lang.util.Math.pow(int, int)",
                (function, positionalParams, unused, pattern, semanticContext, thisIsStatic) -> {
                    Value rawBase = positionalParams.get(0).getValue();
                    Value rawExponent = positionalParams.get(1).getValue();

                    if(rawBase.isExplicit() && rawExponent.isExplicit()) {
                        int base = ((IntegerValue) rawBase).getRawValue();
                        int exponent = ((IntegerValue) rawExponent).getRawValue();

                        return new IntegerValue((int) Math.pow(base, exponent), semanticContext);
                    } else {
                        semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Math::pow hasn't been defined for implicit values yet btw.", pattern));
                    }
                    return null;
                }
        );
        //World
        methods.put("craftr.lang.World.getDayTime()",
                (function, unused, unusedToo, pattern, semanticContext, thisIsKindaStatic) -> {

                    //TODO START REBUILDING EVERYTHING FROM HERE PLEASE
                    //TODO EVERYTHING ELSE IS A NIGHTMARE HELP
                    //TODO THE ROAD TO NORMALITY STARTS HERE

                    //ResolvedObjectiveReference reference = semanticContext.resolve(semanticContext.getAnalyzer().getCompiler().getDataPackBuilder().getScoreHolderManager().RETURN.GENERIC.get());

                    LocalScore score = semanticContext.getGlobalObjectiveManager().RETURN;

                    ExecuteCommand exec = new ExecuteCommand(new TimeQueryCommand(TimeQueryCommand.TimeCounter.DAYTIME));
                    exec.addModifier(new ExecuteStoreScore(score));
                    function.append(exec);

                    return new IntegerValue(new ScoreReference(score), semanticContext);

                    //return new IntegerValue(reference.getUnresolvedObjectiveReference(), semanticContext);
                    //return null;
                });
        methods.put("craftr.lang.World.print(craftr.lang.String)",
                (function, positionalParams, unused, pattern, semanticContext, thisIsKindaStatic) -> {

                    ActualParameter message = positionalParams.get(0);
                    DataReference reference = message.getValue().getReference();
                    if(reference instanceof ExplicitString) {
                        TextComponent text = new StringTextComponent(((ExplicitString) reference).getValue());
                        function.append(new TellrawCommand(new GenericEntity(new Selector(Selector.BaseSelector.ALL_PLAYERS)), text));
                    } else {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Implicit string parameter not supported"));
                    }
                    return null;
                });
        //Entity Base
        methods.put("craftr.lang.entities.entity_base.getAir()",
                (function, unused, unusedToo, pattern, semanticContext, instance) ->
                        new IntegerValue(new NBTReference(((ObjectInstance) instance).getEntity(),new NBTPath("Air")), semanticContext)
        );
        methods.put("craftr.lang.entities.entity_base.kill(boolean)",
                (function, positionalParams, unused, pattern, semanticContext, instance) -> {

                    Entity entity = ((ObjectInstance) instance).getEntity();

                    DataReference seamless = positionalParams.get(0).getValue().getReference();
                    System.out.println("seamless = " + seamless);
                    if(seamless instanceof ExplicitValue) {
                        if(seamless instanceof ExplicitBoolean) {
                            if(((ExplicitBoolean) seamless).getValue()) {
                                function.append(new TeleportCommand(entity, new BlockDestination(new CoordinateSet(0, -512, 0))));
                            } else {
                                function.append(new KillCommand(entity));
                            }
                        } else {
                            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Got non-boolean explicit data reference: " + seamless.getClass().getSimpleName(), pattern));
                        }
                    } else {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Implicit parameters not currently supported for this method", pattern));
                    }
                    return null;
                }
        );
        methods.put("craftr.lang.entities.living_base.setInvisible(boolean)",
                (function, positionalParams, unused, pattern, semanticContext, instance) -> {

                    Entity entity = ((ObjectInstance) instance).getEntity();

                    EffectType invisibility = semanticContext.getModule().minecraft.getTypeManager().effect.get("invisibility");

                    DataReference invisible = positionalParams.get(0).getValue().getReference();
                    if(invisible instanceof ExplicitValue) {
                        if(invisible instanceof ExplicitBoolean) {
                            if(((ExplicitBoolean) invisible).getValue()) {
                                function.append(new EffectGiveCommand(entity, new StatusEffect(invisibility, 100000, 0, StatusEffect.ParticleVisibility.HIDDEN)));
                            } else {
                                function.append(new EffectClearCommand(entity, invisibility));
                            }
                        } else {
                            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Got non-boolean explicit data reference: " + invisible.getClass().getSimpleName(), pattern));
                        }
                    } else {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Implicit parameters not currently supported for this method", pattern));
                    }
                    return null;
                }
        );
        methods.put("craftr.lang.entities.entity_base.setInvulnerable(boolean)",
                (function, positionalParams, unused, pattern, semanticContext, instance) -> {

                    Entity entity = ((ObjectInstance) instance).getEntity();

                    NBTPath path = new NBTPath("Invulnerable");

                    DataReference invulnerable = positionalParams.get(0).getValue().getReference();
                    if(invulnerable instanceof ExplicitValue) {
                        if(invulnerable instanceof ExplicitBoolean) {
                            NBTCompoundBuilder cb = new NBTCompoundBuilder();
                            cb.put(path, new TagByte(((ExplicitBoolean) invulnerable).getValue() ? 1 : 0));
                            function.append(new DataMergeCommand(entity, cb.getCompound()));
                        } else {
                            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Got non-boolean explicit data reference: " + invulnerable.getClass().getSimpleName(), pattern));
                        }
                    } else {
                        invulnerable.toNBT(function, entity, path, semanticContext);
                    }
                    return null;
                }
        );
        methods.put("craftr.lang.entities.armor_stand.setInvisible(boolean)",
                (function, positionalParams, unused, pattern, semanticContext, instance) -> {

                    Entity entity = ((ObjectInstance) instance).getEntity();

                    NBTPath path = new NBTPath("Invisible");

                    DataReference invisible = positionalParams.get(0).getValue().getReference();
                    if(invisible instanceof ExplicitValue) {
                        if(invisible instanceof ExplicitBoolean) {
                            NBTCompoundBuilder cb = new NBTCompoundBuilder();
                            cb.put(path, new TagByte(((ExplicitBoolean) invisible).getValue() ? 1 : 0));
                            function.append(new DataMergeCommand(entity, cb.getCompound()));
                        } else {
                            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Got non-boolean explicit data reference: " + invisible.getClass().getSimpleName(), pattern));
                        }
                    } else {
                        invisible.toNBT(function, entity, path, semanticContext);
                    }
                    return null;
                }
        );
        methods.put("craftr.lang.entities.entity_base.setCustomName(craftr.lang.String)",
                (function, positionalParams, unused, pattern, semanticContext, instance) -> {

                    Entity entity = ((ObjectInstance) instance).getEntity();

                    DataReference customName = positionalParams.get(0).getValue().getReference();
                    if(customName instanceof ExplicitString) {
                        function.append(new DataMergeCommand(entity, new TagCompound(new TagString("CustomName", ((ExplicitString) customName).getValue()))));
                    } else {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Implicit parameters not currently supported for this method", pattern));
                    }
                    return null;
                }
        );
    }

    public static Value execute(Method method, Function function, List<ActualParameter> positionalParams, HashMap<String, ActualParameter> keywordParams, TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder) {
        String fullSignature = method.getSignature().getFullyQualifiedName();
        MethodExecutor executor = methods.get(fullSignature);
        if(executor != null) {
            return executor.writeCall(function, positionalParams, keywordParams, pattern, semanticContext, dataHolder);
        }
        else semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Native Methods", NoticeType.INFO, "Require native implementation for '" + fullSignature + "'", pattern));
        return null;
    }
}
