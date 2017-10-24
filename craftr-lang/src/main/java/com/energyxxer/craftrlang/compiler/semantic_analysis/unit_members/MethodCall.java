package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.code_generation.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.ScoreboardCommand;
import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.ScoreboardOperation;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.commands.SelectorReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodCall extends Value implements FunctionWriter, TraversableStructure {
    private final TokenPattern<?> pattern;
    private String methodName;
    private ArrayList<ActualParameter> positionalParams = new ArrayList<>();
    private HashMap<String, ActualParameter> keywordParams = new HashMap<>();

    private Method method = null;

    public MethodCall(TokenPattern<?> pattern, DataHolder dataHolder, MCFunction function, Context context) {
        super(context);
        this.pattern = pattern;
        this.methodName = ((TokenItem) pattern.find("METHOD_CALL_NAME")).getContents().value;

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
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal keyword parameter label", rawLabel.getFormattedPath()));
                        }
                    }
                    TokenPattern<?> rawValue = rawParam.find("VALUE");
                    Value value = ExprResolver.analyzeValueOrReference(rawValue, context, (!context.isStatic()) ? context.getUnit().getGenericInstance() : null, function);

                    if(label == null) {
                        positionalParams.add(new ActualParameter(rawParam, value));
                        if(value == null) {
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Actual positional parameter is null: " + rawValue, rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context:" + context, rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context.isStatic():" + context.isStatic(), rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context.getUnit():" + context.getUnit(), rawValue.getFormattedPath()));
                        }
                    } else {
                        if(keywordParams.containsKey(label)) {
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate keyword parameter", rawLabel.getFormattedPath()));
                        }
                        keywordParams.put(label, new ActualParameter(rawParam, label, value));
                    }
                }
            }
        }

        ArrayList<FormalParameter> formalParams = new ArrayList<>();

        positionalParams.forEach(p -> formalParams.add(p.toFormal()));

        if(dataHolder.getMethodLog() == null) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method from an undefined data holder", pattern.getFormattedPath()));
        } else {
            MethodSignature signature = new MethodSignature(dataHolder.getMethodLog().getDeclaringUnit(), methodName, formalParams);

            this.method = dataHolder.getMethodLog().findMethod(signature, pattern, context, (dataHolder instanceof ObjectInstance) ? ((ObjectInstance) dataHolder) : null);
        }

        if(method != null && method.getReturnType() != DataType.VOID) {
            this.reference = new ObjectivePointer(new SelectorReference(context.getAnalyzer().getPrefix() + "_RETURN",context),context.getAnalyzer().getPrefix() + "_g");
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
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        return null;
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        if(method != null) {
            if(method.isStatic()) {
                String argPlayer = context.getAnalyzer().getPrefix() + "_" + method.getPlayerName();
                String argObjective = context.getAnalyzer().getPrefix() + "_a";
                for(int i = 0; i < positionalParams.size(); i++) {
                    ActualParameter rawParam = positionalParams.get(i);
                    Value actualParam = rawParam.getValue();
                    if(actualParam == null) {
                        function.addComment("Actual param is null: " + rawParam);
                        continue;
                    }
                    actualParam = actualParam.unwrap(function);
                    if(actualParam == null) {
                        function.addComment("Actual unwrapped param is null: " + rawParam);
                    } else if(actualParam.isExplicit()) {
                        function.addInstruction(new ScoreboardCommand(new ObjectivePointer(new SelectorReference(argPlayer, context), argObjective+i),ScoreboardCommand.SET,actualParam));
                    } else {
                        function.addInstruction(new ScoreboardOperation(new ObjectivePointer(new SelectorReference(argPlayer, context), argObjective+i),ScoreboardOperation.ASSIGN,actualParam.getReference()));
                    }
                    rawParam.setValue(actualParam);
                }
                //TODO: Keyword Parameters
            } else {
                function.addComment(method.getName() + " is not static");
            }

            return method.writeCall(function, positionalParams, keywordParams, pattern, context);
        }
        return null;
    }

    @Override
    public Value unwrap(MCFunction function) {
        Value value = this.writeToFunction(function);
        return (value != null) ? value.unwrap(function) : null;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        Value unwrapped = this.unwrap(function);
        if(unwrapped != null) return unwrapped.runOperation(operator, pattern, function, silent);
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, MCFunction function, boolean silent) {
        Value unwrapped = this.unwrap(function);
        if(unwrapped != null) return unwrapped.runOperation(operator, value, pattern, function, silent);
        else return null;
    }

    @Override
    public ObjectivePointer getReference() {
        throw new IllegalStateException("Dude, you shouldn't access a method call reference directly, first unwrap.");
    }
}
