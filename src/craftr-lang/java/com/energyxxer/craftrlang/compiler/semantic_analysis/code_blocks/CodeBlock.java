package com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks;

import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class CodeBlock implements Context {
    private CraftrFile declaringFile;
    private CodeBlock parentBlock = null;
    private SymbolTable symbolTable;

    public CodeBlock(CraftrFile file, SymbolTable parentTable) {
        this.declaringFile = file;
        this.symbolTable = new SymbolTable(SymbolVisibility.BLOCK, parentTable);
    }

    public CodeBlock(CraftrFile file, CodeBlock parentBlock) {
        this.declaringFile = file;
        this.parentBlock = parentBlock;
        this.symbolTable = new SymbolTable(SymbolVisibility.BLOCK, parentBlock.getSymbolTable());
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Variable findVariable(String name) {
        Symbol inCurrent = symbolTable.getSymbol(name, this);
        return null;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return declaringFile;
    }

    @Override
    public ContextType getType() {
        return ContextType.BLOCK;
    }
}
