package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
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

    private Value value = null;

    public Variable(TokenPattern<?> pattern, Context context, SymbolVisibility visibility, List<CraftrLang.Modifier> modifiers, DataType dataType, String name, boolean validName, CodeBlock block, Value value) {
        super(context);
        this.pattern = pattern;
        this.visibility = visibility;
        this.modifiers = modifiers;
        this.dataType = dataType;
        this.name = name;
        this.validName = validName;
        this.block = block;
        this.value = value;
    }

    private Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, Context context) {
        super(context);
        this.pattern = pattern;
        this.modifiers = new ArrayList<>();
        this.modifiers.addAll(modifiers);
        this.dataType = dataType;

        this.visibility = (
                (modifiers.contains(CraftrLang.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrLang.Modifier.PROTECTED) ? SymbolVisibility.UNIT_INHERITED :
                modifiers.contains(CraftrLang.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                context instanceof CodeBlock ? SymbolVisibility.BLOCK : SymbolVisibility.PACKAGE));

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;
        this.validName = !CraftrLang.isPseudoIdentifier(this.name);

        if(!validName) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal variable name", pattern.find("VARIABLE_NAME").getFormattedPath()));
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, CodeBlock block) {
        this(pattern, modifiers, dataType, (Context) block);

        this.block = block;

        if(validName) {
            if(block.findVariable(((TokenItem) pattern.find("VARIABLE_NAME")).getContents()) == null) {
                block.getSymbolTable().put(this);
            } else {
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath()));
            }
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, Unit parentUnit) {
        this(pattern, modifiers, dataType, (Context) parentUnit);

        if(validName) {
            /*if(fieldLog.findField(((TokenItem) pattern.find("VARIABLE_NAME")).getContents()) == null) {
                table.put(this);
            } else {
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath()));
            }*/
        }

        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report", NoticeType.INFO, name + ": " + this.value, pattern.getFormattedPath()));
    }

    public void initializeValue() {
        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {

            MCFunction initializerFunction;
            if(context instanceof Unit) {
                if(modifiers.contains(CraftrLang.Modifier.STATIC))
                    initializerFunction = ((Unit) context).getStaticInitializer();
                else
                    initializerFunction = ((Unit) context).getInstanceInitializer();
            } else if(context instanceof CodeBlock) {
                initializerFunction = ((CodeBlock) context).getFunction();
            } else {
                initializerFunction = null;
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Something went wrong: Variable context is of unrecognized type: " + context.getClass().getSimpleName(), pattern.getFormattedPath()));
                return;
            }

            this.value = ExprResolver.analyzeValue(initialization.find("VALUE"), context, (context instanceof Unit && !this.isStatic()) ? ((Unit) context).getGenericInstance() : null, initializerFunction);
            if(this.value != null && !this.dataType.instanceOf(this.value.getDataType())) {
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE").getFormattedPath()));
            }
        }
    }

    /*
    * The reason why in the Code block variable constructor, it calls the CodeBlock::findVariable(String) method instead
    * of the usual SymbolTable::getSymbol(String, Context), like in the unit variable constructor, is that to find a
    * field in a unit, you can just look at the unit's single symbol table, whereas in the code block, you have to trace
    * back several nested code blocks to find the variable declared several scopes up.
    *
    * TODO: FIX THIS EYESORE OF A MESS
    * */

    //Local variable parsing
    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, CodeBlock block) {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrLang.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("VARIABLE_INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), block.getAnalyzer());

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("VARIABLE_INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, null, block));
        }

        return variables;
    }

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
        return new Variable(pattern, context, visibility, modifiers, dataType, name, validName, block, value);
    }

    public Value getValue() {
        return value;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        return null;
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
        if(context instanceof CodeBlock) return context.getUnit(); else if(context instanceof Unit) return ((Unit) context);
        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Variable with no unit: " + this, pattern.getFormattedPath()));
        return null;
    }
}
