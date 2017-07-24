package com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks;

import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class CodeBlock implements Context {
    private final SemanticAnalyzer analyzer;
    private final Unit unit;
    private final CraftrFile declaringFile;

    private CodeBlock parentBlock = null;
    private SymbolTable symbolTable;

    public CodeBlock(Unit unit, SymbolTable parentTable) {
        this.unit = unit;
        this.declaringFile = unit.getDeclaringFile();
        this.analyzer = declaringFile.getAnalyzer();
        this.symbolTable = new SymbolTable(SymbolVisibility.BLOCK, parentTable);
    }

    public CodeBlock(Unit unit, CodeBlock parentBlock) {
        this.unit = unit;
        this.declaringFile = unit.getDeclaringFile();
        this.analyzer = declaringFile.getAnalyzer();
        this.parentBlock = parentBlock;
        this.symbolTable = new SymbolTable(SymbolVisibility.BLOCK, parentBlock.getSymbolTable());
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Variable findVariable(String name) {
        Symbol inCurrent = symbolTable.getSymbol(name, this);
        if(inCurrent != null && inCurrent instanceof Variable) return (Variable) inCurrent;
        else if(parentBlock != null) return parentBlock.findVariable(name);
        return null;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return declaringFile;
    }

    @Override
    public Unit getUnit() {
        return unit;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.BLOCK;
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return analyzer;
    }
}
