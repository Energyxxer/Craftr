package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.exceptions.CompilerException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprParser;
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

    private SymbolVisibility visibility = SymbolVisibility.BLOCK;
    private List<CraftrUtil.Modifier> modifiers;

    private DataType dataType;
    private String name;

    private CodeBlock block = null;
    private Unit unit = null;

    private Value value = null;

    private SymbolTable table;

    private Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType) {
        super(pattern);
        this.modifiers = new ArrayList<>();
        this.modifiers.addAll(modifiers);
        this.dataType = dataType;

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;

        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {
            this.value = ExprParser.parseValue(initialization.find("VALUE"));
            if(this.value != null && this.dataType != this.value.getDataType()) {
                CompilerException x = new CompilerException("Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE").getFormattedPath());
                x.setErrorCode("INCOMPATIBLE_TYPES");
                throw x;
            }
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType, CodeBlock block) throws CompilerException {
        this(pattern, modifiers, dataType);

        this.block = block;
        this.table = block.getSymbolTable();

        if(block.findVariable(this.name) == null) {
            block.getSymbolTable().put(this);
        } else {
            CompilerException x = new CompilerException("Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath());
            x.setErrorCode("VARIABLE_ALREADY_DEFINED");
            throw x;
        }
    }

    public Variable(TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType dataType, Unit unit) throws CompilerException {
        this(pattern, modifiers, dataType);

        this.unit = unit;
        this.table = unit.getSubSymbolTable();

        if(!table.getMap().containsKey(this.name)) {
            table.put(this);
        } else {
            CompilerException x = new CompilerException("Variable '" + name + "' already declared in the scope", this.pattern.find("VARIABLE_NAME").getFormattedPath());
            x.setErrorCode("VARIABLE_ALREADY_DEFINED");
            throw x;
        }

        unit.getDeclaringFile().getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report: ", NoticeType.INFO, name + ": " + this.value, pattern.getFormattedPath()));
    }

    /*
    * The reason why in the Code block variable constructor, it calls the CodeBlock::findVariable(String) method instead
    * of the usual SymbolTable::getSymbol(String, Context), like in the unit variable constructor, is that to find a
    * field in a unit, you can just look at the unit's single symbol table, whereas in the code block, you have to trace
    * back several nested code blocks to find the variable declared several scopes up.
    * */

    //Local variable parsing
    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, CodeBlock block) throws CompilerException  {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()));

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, null, block));
        }

        return variables;
    }

    public static List<Variable> parseDeclaration(TokenPattern<?> pattern, Unit unit) throws CompilerException {
        ArrayList<Variable> variables = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()));

        DataType dataType = DataType.parseType((pattern.find("INNER.DATA_TYPE")).flatten(false), unit.getSubSymbolTable());

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            variables.add(new Variable(p, modifiers, dataType, unit));
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
