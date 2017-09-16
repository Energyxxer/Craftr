package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class Variable extends AbstractFileComponent implements Symbol {
    private final Context context;

    private SymbolVisibility visibility = SymbolVisibility.BLOCK;
    private List<CraftrUtil.Modifier> modifiers;

    private DataType dataType;
    private String name;

    private CodeBlock block = null;
    private FieldManager fieldManager = null;

    private Value value = null;

    private SymbolTable table;

    private Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType, Context context) {
        super(pattern);
        this.modifiers = new ArrayList<>();
        this.modifiers.addAll(modifiers);
        this.dataType = dataType;
        this.context = context;

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;

        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {
            this.value = ExprAnalyzer.analyzeValue(initialization.find("VALUE"), context);
            if(this.value != null && !this.dataType.instanceOf(this.value.getDataType())) {
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE").getFormattedPath()));
            }
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType, CodeBlock block) {
        this(pattern, modifiers, dataType, (Context) block);

        this.block = block;
        this.table = block.getSymbolTable();

        if(block.findVariable(((TokenItem) pattern.find("VARIABLE_NAME")).getContents()) == null) {
            block.getSymbolTable().put(this);
        } else {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath()));
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType, FieldManager fieldManager) {
        this(pattern, modifiers, dataType, fieldManager.getParentUnit());

        this.fieldManager = fieldManager;
        this.table = this.isStatic() ? fieldManager.getStaticFieldTable() : fieldManager.getInstanceFieldTable();

        if(fieldManager.findField(((TokenItem) pattern.find("VARIABLE_NAME")).getContents()) == null) {
            table.put(this);
        } else {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath()));
        }

        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report: ", NoticeType.INFO, name + ": " + this.value, pattern.getFormattedPath()));
    }

    /*
    * The reason why in the Code block variable constructor, it calls the CodeBlock::findVariable(String) method instead
    * of the usual SymbolTable::getSymbol(String, Context), like in the unit variable constructor, is that to find a
    * field in a unit, you can just look at the unit's single symbol table, whereas in the code block, you have to trace
    * back several nested code blocks to find the variable declared several scopes up.
    * */

    //Local variable parsing
    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, CodeBlock block) {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), block.getAnalyzer());

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, null, block));
        }

        return variables;
    }

    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, FieldManager fieldManager, Context context) {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), fieldManager.getParentUnit().getAnalyzer());

        DataType dataType = DataType.parseType((pattern.find("INNER.DATA_TYPE")).flattenTokens(), fieldManager.getParentUnit().getDeclaringFile().getReferenceTable(), context);

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, dataType, fieldManager));
        }

        return variables;
    }

    public boolean isStatic() {
        return modifiers.contains(CraftrUtil.Modifier.STATIC);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @Nullable SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public SymbolVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SymbolVisibility visibility) {
        this.visibility = visibility;
    }
}
