package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.statements.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Null;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class Variable extends Value implements Symbol, DataHolder, TraversableStructure {
    public final TokenPattern<?> pattern;

    private SymbolVisibility visibility = SymbolVisibility.BLOCK;
    private List<CraftrLang.Modifier> modifiers;

    private DataType dataType;
    private String name;
    private boolean validName;

    private CodeBlock block = null;
    private Method method = null;

    private Value value;

    public Variable(TokenPattern<?> pattern, SemanticContext semanticContext, SymbolVisibility visibility, List<CraftrLang.Modifier> modifiers, DataType dataType, String name, boolean validName, CodeBlock block, Method method, Value value) {
        super(semanticContext);
        this.pattern = pattern;
        this.visibility = visibility;
        this.modifiers = modifiers;
        this.dataType = dataType;
        this.name = name;
        this.validName = validName;
        this.block = block;
        this.method = method;
        if(value == null) this.value = new Null(semanticContext);
        else this.value = value;
    }

    private Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, SemanticContext semanticContext) {
        super(semanticContext);
        this.pattern = pattern;
        this.modifiers = new ArrayList<>(modifiers);
        this.dataType = dataType;

        this.visibility = (
                (modifiers.contains(CraftrLang.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrLang.Modifier.PROTECTED) ? SymbolVisibility.UNIT_INHERITED :
                modifiers.contains(CraftrLang.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                semanticContext instanceof CodeBlock ? SymbolVisibility.BLOCK : SymbolVisibility.PACKAGE));

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;
        this.validName = !CraftrLang.isPseudoIdentifier(this.name);

        if(!validName) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal variable name", pattern.find("VARIABLE_NAME").getFormattedPath()));
        }
        this.value = new Null(semanticContext);
    }

    public Variable(String name, List<CraftrLang.Modifier> modifiers, DataType dataType, Method method, Value value) {
        super(method);
        this.pattern = null;
        this.visibility = SymbolVisibility.METHOD;
        this.modifiers = new ArrayList<>(modifiers);
        this.dataType = dataType;
        this.name = name;
        this.validName = !CraftrLang.isPseudoIdentifier(this.name);
        this.block = null;
        this.method = method;
        if(value == null) this.value = new Null(semanticContext);
        else this.value = value;
    }

    public Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, Unit parentUnit) {
        this(pattern, modifiers, dataType, (SemanticContext) parentUnit);

    }

    public void initializeValue() {
        if(this.pattern == null) return;
        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {

            Function initializerFunction;
            if(semanticContext instanceof Unit) {
                if(modifiers.contains(CraftrLang.Modifier.STATIC))
                    initializerFunction = ((Unit) semanticContext).getStaticInitializer();
                else
                    initializerFunction = ((Unit) semanticContext).getInstanceInitializer();
            } else if(semanticContext instanceof CodeBlock) {
                initializerFunction = ((CodeBlock) semanticContext).getFunction();
            } else if(semanticContext instanceof Method) {
                return; //There's no variable initialization in method parameters;
                        // also this class isn't the one that parses method params so no point in having this;
            } else {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Something went wrong: Variable semanticContext is of unrecognized type: " + semanticContext.getClass().getSimpleName(), pattern.getFormattedPath()));
                return;
            }

            this.value = ExprResolver.analyzeValue(initialization.find("VALUE"), (semanticContext instanceof Unit && !isStatic()) ? ((Unit) semanticContext).getInstanceSemanticContext() : semanticContext, null, initializerFunction);
            if(this.value != null) {
                this.value = this.value.unwrap(initializerFunction);
            }
            if(this.value != null && !this.value.getDataType().instanceOf(this.getDataType())) {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE").getFormattedPath()));
                this.value = null;
            }
            if(this.value == null) this.value = new Null(semanticContext);
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report", NoticeType.INFO, name + ": " + this.value, pattern.getFormattedPath()));
        }
    }

    /*
    * The reason why in the Code block variable constructor, it calls the CodeBlock::findVariable(String) method instead
    * of the usual SymbolTable::getSymbol(String, SemanticContext), like in the unit variable constructor, is that to find a
    * field in a unit, you can just look at the unit's single symbol table, whereas in the code block, you have to trace
    * back several nested code blocks to find the variable declared several scopes up.
    *
    * TODO: FIX THIS EYESORE OF A MESS
    * */

    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, Unit unit) {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrLang.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("VARIABLE_INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), unit.getAnalyzer());

        DataType dataType = DataType.parseType((pattern.find("VARIABLE_INNER.DATA_TYPE")).flattenTokens(), unit.getDeclaringFile().getReferenceTable(), unit);

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("VARIABLE_INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, dataType, unit));
        }

        return variables;
    }

    public Variable duplicate() {
        return new Variable(pattern, semanticContext, visibility, modifiers, dataType, name, validName, block, method, value);
    }

    public String getObjectiveName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean silent) {
        //fromVariable SHOULD be false
        if(operand != null && operand instanceof Variable) {
            operand = ((Variable) operand).value;
        }
        if(operand == null) {
            if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Operand is null", pattern.getFormattedPath()));
            return null;
        }
        /*if(operator.isAssignment()) {
            switch(operator) {
                case ASSIGN: {
                    if(operand.getDataType().instanceOf(this.getDataType())) {
                        value = operand;
                    } else {
                        if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern.getFormattedPath()));
                    }
                    return value;
                }
                default: return null;
            }
        } else {
        }*/
        if(operand.isNull() && operator == Operator.ASSIGN) {
            this.value = operand;
            return value;
        } else if(value.isNull() && operator == Operator.ASSIGN) {
            if(operand.getDataType().instanceOf(this.getDataType())) {
                value = operand.clone(function);
            } else {
                if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern.getFormattedPath()));
            }
            return value.clone(function);
        }
        if(!value.isNull()) return value.runOperation(operator, operand, pattern, function, silent);
        else {
            if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable might not have been defined", pattern.getFormattedPath()));
            return value;
        }
    }

    @Override
    public boolean isExplicit() {
        return value != null && value.isExplicit();
    }

    public boolean isStatic() {
        return modifiers.contains(CraftrLang.Modifier.STATIC);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable SymbolTable getSubSymbolTable() {
        return (value != null) ? value.getSubSymbolTable() : null;
    }

    @Override
    public MethodLog getMethodLog() {
        return (value != null) ? value.getMethodLog() : null;
    }

    @Override
    public SymbolVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SymbolVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public @Nullable Package getPackage() {
        if(getUnit() != null) return getUnit().getPackage();
        return null;
    }

    @Override
    public @Nullable Unit getUnit() {
        if(semanticContext instanceof CodeBlock) return semanticContext.getUnit(); else if(semanticContext instanceof Unit) return ((Unit) semanticContext);
        semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Variable with no unit: " + this, pattern.getFormattedPath()));
        return null;
    }

    @Override
    public Value clone(Function function) {
        throw new IllegalStateException("Dude, don't clone the variable, clone the value!");
    }
}
