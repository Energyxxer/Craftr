package com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.statements.Statement;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class CodeBlock extends Statement implements Context {
    private boolean staticBlock = false;

    private CodeBlock parentBlock = null;
    private SymbolTable symbolTable;

    private boolean initialized = false;

    public CodeBlock(TokenPattern<?> pattern, Context context) {
        super(pattern, context, new MCFunction((context instanceof Method) ? "func@" + ((Method) context).getName() : ((context instanceof CodeBlock) ? ((CodeBlock) context).function.getName() : "func@" + context.getDeclaringFile().getIOFile().getName())));

        if(context instanceof CodeBlock) this.parentBlock = (CodeBlock) context;

        this.symbolTable = new SymbolTable(SymbolVisibility.BLOCK, (parentBlock != null) ? parentBlock.symbolTable : context.getUnit().getSubSymbolTable());
    }

    public void initialize() {
        if(initialized) return;
        TokenPattern<?> inner = (TokenPattern<?>) pattern.getContents();

        TokenList list = (TokenList) inner.find("STATEMENT_LIST");
        if(list != null) {
            TokenPattern<?>[] rawStatements = list.getContents();
            for(TokenPattern<?> rawStatement : rawStatements) {
                if(!(rawStatement instanceof TokenStructure)) continue;

                Statement statement = Statement.read(((TokenStructure) rawStatement).getContents(), this, function);

                if(statement != null) {
                    statement.writeToFunction(function); //TEMPORARY. DO MORE STUFF OFC
                    System.out.println(statement.getClass().getSimpleName());
                }
            }
        }

        initialized = true;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Variable findVariable(Token name) {
        Symbol inCurrent = symbolTable.getSymbol(name, this);
        if(inCurrent != null && inCurrent instanceof Variable) return (Variable) inCurrent;
        else if(parentBlock != null) return parentBlock.findVariable(name);
        return null;
    }

    public void setStatic(boolean staticBlock) {
        this.staticBlock = staticBlock;
    }

    public MCFunction getFunction() {
        return function;
    }

    public int getLevel() {
        return (parentBlock != null) ? parentBlock.getLevel()+1 : 0;
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        function.addFunction(this.function);
        return null;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.BLOCK;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return context.getDeclaringFile();
    }

    @Override
    public Unit getUnit() {
        return context.getUnit();
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return context.getAnalyzer();
    }

    @Override
    public boolean isStatic() {
        return staticBlock;
    }

    @Override
    public Context getParent() {
        return context;
    }

    @Override
    public SymbolTable getReferenceTable() {
        return null;
    }
}
