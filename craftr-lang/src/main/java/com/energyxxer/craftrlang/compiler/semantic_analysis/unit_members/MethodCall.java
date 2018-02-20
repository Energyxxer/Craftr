package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
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
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodCall extends Value implements TraversableStructure {
    private final TokenPattern<?> pattern;
    private String methodName;
    private ArrayList<ActualParameter> positionalParams = new ArrayList<>();
    private HashMap<String, ActualParameter> keywordParams = new HashMap<>();
    private DataHolder dataHolder = null;
    private Method method = null;

    public MethodCall(TokenPattern<?> pattern, DataHolder dataHolder, Function function, SemanticContext semanticContext) {
        super(semanticContext);
        this.pattern = pattern;
        this.methodName = ((TokenItem) pattern.find("METHOD_CALL_NAME")).getContents().value;

        if(dataHolder instanceof Unit) {
            dataHolder = ((Unit) dataHolder).getDataHolder(); //Unwrap one more time to get the instance data holder of singleton units
        }
        this.dataHolder = dataHolder;

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
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal keyword parameter label", rawLabel.getFormattedPath()));
                        }
                    }
                    TokenPattern<?> rawValue = rawParam.find("VALUE");
                    Value value = ExprResolver.analyzeValue(rawValue, semanticContext, null, function);

                    if(label == null) {
                        positionalParams.add(new ActualParameter(rawParam, value));
                        if(value == null) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Actual positional parameter is null: " + rawValue, rawValue.getFormattedPath()));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext:" + semanticContext, rawValue.getFormattedPath()));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext.isStatic():" + semanticContext.isStatic(), rawValue.getFormattedPath()));
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... semanticContext.getUnit():" + semanticContext.getUnit(), rawValue.getFormattedPath()));
                        }
                    } else {
                        if(keywordParams.containsKey(label)) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate keyword parameter", rawLabel.getFormattedPath()));
                        }
                        keywordParams.put(label, new ActualParameter(rawParam, label, value));
                    }
                }
            }
        }

        ArrayList<FormalParameter> formalParams = new ArrayList<>();

        positionalParams.forEach(p -> formalParams.add(p.toFormal()));

        if(dataHolder.getMethodLog() == null) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method from an undefined data holder", pattern.getFormattedPath()));
        } else {
            MethodSignature signature = new MethodSignature(dataHolder.getMethodLog().getDeclaringUnit(), methodName, formalParams);

            this.method = dataHolder.getMethodLog().findMethod(signature, pattern, semanticContext, (dataHolder instanceof ObjectInstance) ? ((ObjectInstance) dataHolder) : null);
        }

        if(method != null && method.getReturnType() != DataType.VOID) {
            //this.reference = semanticContext.getAnalyzer().getCompiler().getDataPackBuilder().getScoreHolderManager().RETURN.GENERIC.get();
        }
    }

    @Override
    public boolean isExplicit() {
        return false;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public DataType getDataType() {
        return (method != null) ? method.getReturnType() : DataType.VOID;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return (method != null) ? method.getReturnType().getSubSymbolTable() : null;
    }

    @Override
    public MethodLog getMethodLog() {
        return (method != null) ? method.getReturnType().getMethodLog() : null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public Value unwrap(Function function) {
        return (method != null) ? method.writeCall(function, positionalParams, keywordParams, pattern, semanticContext, dataHolder) : null;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        Value unwrapped = this.unwrap(function);
        if(unwrapped != null) return unwrapped.runOperation(operator, pattern, function, fromVariable, silent);
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        Value unwrapped = this.unwrap(function);
        if(unwrapped != null) return unwrapped.runOperation(operator, value, pattern, function, fromVariable, silent);
        else return null;
    }

    @Override
    public LocalScore getReference() {
        throw new IllegalStateException("Mate, don't access a method call reference directly, first unwrap.");
    }

    @Override
    public Value clone(Function function) {
        throw new IllegalStateException("Mate, don't clone the method call, first unwrap.");
    }
}
