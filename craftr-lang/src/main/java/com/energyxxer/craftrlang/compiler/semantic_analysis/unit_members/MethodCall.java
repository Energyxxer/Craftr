package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodCall implements TraversableStructure {
    private final TokenPattern<?> pattern;
    private final SemanticContext semanticContext;
    private String methodName;
    private ArrayList<ActualParameter> positionalParams = new ArrayList<>();
    private HashMap<String, ActualParameter> keywordParams = new HashMap<>();
    private DataHolder dataHolder;
    private Method method = null;
    private Function function;

    public MethodCall(TokenPattern<?> pattern, DataHolder dataHolder, Function function, SemanticContext semanticContext) {
        this.semanticContext = semanticContext;
        this.pattern = pattern;
        this.function = function;
        this.methodName = ((TokenItem) pattern.find("METHOD_CALL_NAME")).getContents().value;

        if(dataHolder instanceof Unit) {
            dataHolder = ((Unit) dataHolder).getDataHolder(); //Unwrap one more time to get the instance data holder of singleton units
        }
        this.dataHolder = dataHolder;

        if(methodName.equals("this")) {
            if(dataHolder instanceof Unit) methodName = ((Unit) dataHolder).getName(); //For consistency with field-handling of 'this';
            else methodName = semanticContext.getUnit().getName();
        }

        TokenList parameterListWrapper = (TokenList) pattern.find("PARAMETER_LIST");

        if(parameterListWrapper != null) {
            TokenPattern<?>[] parameterList = parameterListWrapper.getContents();
            for(TokenPattern<?> entry : parameterList) {
                if(entry.getName().equals("PARAMETER")) {
                    TokenGroup rawParam = (TokenGroup) entry;

                    String label = null;

                    TokenPattern<?> rawLabel = rawParam.find("PARAMETER_LABEL_WRAPPER");
                    if(rawLabel != null) {
                        label = ((TokenItem) rawLabel.find("PARAMETER_LABEL")).getContents().value;
                        if(CraftrLang.isPseudoIdentifier(label)) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal keyword parameter label", rawLabel));
                        }
                    }
                    TokenPattern<?> rawValue = rawParam.find("VALUE");
                    Value value = ExprResolver.analyzeValue(rawValue, semanticContext, null, function);

                    if(label == null) {
                        positionalParams.add(new ActualParameter(rawParam, value));
                        if(value == null) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Actual positional parameter is null: " + rawValue, rawValue));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext:" + semanticContext, rawValue));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext.isStatic():" + semanticContext.isStatic(), rawValue));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext.getUnit():" + semanticContext.getUnit(), rawValue));
                        }
                    } else {
                        if(keywordParams.containsKey(label)) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate keyword parameter", rawLabel));
                        }
                        keywordParams.put(label, new ActualParameter(rawParam, label, value));
                    }
                }
            }
        }

        ArrayList<FormalParameter> formalParams = new ArrayList<>();

        positionalParams.forEach(p -> formalParams.add(p.toFormal()));

        if(dataHolder.getMethodLog() == null) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method from an undefined data holder", pattern));
        } else {
            MethodSignature signature = new MethodSignature(dataHolder.getMethodLog().getDeclaringUnit(), methodName, formalParams);

            this.method = dataHolder.getMethodLog().findMethod(signature, pattern, semanticContext, dataHolder.asObjectInstance());
        }

        if(method != null && method.getReturnType() != DataType.VOID) {
            //this.reference = semanticContext.getAnalyzer().getCompiler().getDataPackBuilder().getScoreHolderManager().RETURN.GENERIC.get();
        }
    }

    public Method getMethod() {
        return method;
    }

    public Value evaluate() {
        if(method != null) {
            return method.writeCall(this.function, positionalParams, keywordParams, pattern, semanticContext, dataHolder);
        } else return null;
    }
}
