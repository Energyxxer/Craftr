package com.energyxxer.craftrlang.compiler.semantic_analysis.natives;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.time.TimeQueryCommand;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.ActualParameter;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.IntegerValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.HashMap;
import java.util.List;

public class NativeMethods {
    private static HashMap<String, MethodExecutor> methods = new HashMap<>();

    static {
        // Math
        methods.put("craftr.lang.util.Math.pow(int, int)",
                (function, positionalParams, unused, pattern, context) -> {
                    Value rawBase = positionalParams.get(0).getValue();
                    Value rawExponent = positionalParams.get(1).getValue();

                    if(rawBase.isExplicit() && rawExponent.isExplicit()) {
                        int base = ((IntegerValue) rawBase).getRawValue();
                        int exponent = ((IntegerValue) rawExponent).getRawValue();

                        return new IntegerValue((int) Math.pow(base, exponent), context);
                    } else {
                        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Math::pow hasn't been defined for implicit values yet btw.", pattern.getFormattedPath()));
                    }
                    return null;
                }
        );
        //World
        methods.put("craftr.lang.World.getDayTime()",
                (function, unused, unusedToo, pattern, context) -> {

                    //TODO START REBUILDING EVERYTHING FROM HERE PLEASE
                    //TODO EVERYTHING ELSE IS A NIGHTMARE HELP
                    //TODO THE ROAD TO NORMALITY STARTS HERE

                    //ResolvedObjectiveReference reference = context.resolve(context.getAnalyzer().getCompiler().getDataPackBuilder().getScoreHolderManager().RETURN.GENERIC.get());

                    ExecuteCommand exec = new ExecuteCommand(new TimeQueryCommand(TimeQueryCommand.TimeCounter.DAYTIME));
                    //exec.addModifier(new ExecuteStoreScore(score));
                    function.append(exec);

                    //return new IntegerValue(reference.getUnresolvedObjectiveReference(), context);
                    return null;
                });
    }

    public static Value execute(Method method, Function function, List<ActualParameter> positionalParams, HashMap<String, ActualParameter> keywordParams, TokenPattern<?> pattern, Context context) {
        String fullSignature = method.getSignature().getFullyQualifiedName();
        MethodExecutor executor = methods.get(fullSignature);
        if(executor != null) {
            return executor.writeCall(function, positionalParams, keywordParams, pattern, context);
        }
        else context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Native Methods", NoticeType.INFO, "Require native implementation for '" + fullSignature + "'", method.pattern.getFormattedPath()));
        return null;
    }
}
