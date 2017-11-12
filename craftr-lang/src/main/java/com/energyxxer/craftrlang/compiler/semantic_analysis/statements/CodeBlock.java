package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.players.ScoreHolder;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

import java.util.List;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class CodeBlock extends Statement implements Context, DataHolder {
    private boolean closed = false;

    private CodeBlock parentBlock = null;
    private SymbolTable symbolTable = new SymbolTable() {
        @Override
        public Symbol getSymbol(List<Token> flatTokens, Context context, boolean silent) {
            if(flatTokens.size() > 1) {
                //I don't think this should even be allowed
                if(!silent && !CodeBlock.this.silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Trying to get a symbol of more than one token from a code block...?", flatTokens.get(0).getFormattedPath()));
                return super.getSymbol(flatTokens, context, silent);
            }

            Symbol sym = super.getSymbol(flatTokens, context, true);
            if(sym != null) return sym;
            if(parentBlock != null) return parentBlock.getSymbolTable().getSymbol(flatTokens, context, silent || CodeBlock.this.silent);
            if(isStatic()) return context.getUnit().getStaticFieldLog().getSymbol(flatTokens, context, silent || CodeBlock.this.silent);
            else return context.getUnit().getGenericInstance().getSubSymbolTable().getSymbol(flatTokens, context, silent || CodeBlock.this.silent);
        }
    };

    private boolean initialized = false;

    public CodeBlock(TokenPattern<?> pattern, Context context) {
        super(pattern, context, new MCFunction((context instanceof Method) ? "func@" + ((Method) context).getName() : ((context instanceof CodeBlock) ? ((CodeBlock) context).function.getName() : "func@" + context.getDeclaringFile().getIOFile().getName())));

        if(context instanceof CodeBlock) this.parentBlock = (CodeBlock) context;

        this.clearSymbols();
    }

    public void clearSymbols() {
        this.symbolTable.clear();
    }

    public void initialize() {
        if(initialized) return;
        this.writeToFunction(this.function);
        initialized = true;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public MCFunction getFunction() {
        return function;
    }

    public int getLevel() {
        return (parentBlock != null) ? parentBlock.getLevel()+1 : 0;
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        TokenPattern<?> inner = (TokenPattern<?>) pattern.getContents();

        closed = false;
        Value returnValue = null;

        TokenList list = (TokenList) inner.find("STATEMENT_LIST");
        if(list != null) {
            TokenPattern<?>[] rawStatements = list.getContents();
            for(TokenPattern<?> rawStatement : rawStatements) {
                if(closed) {
                    if(!silent) context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unreachable statement", rawStatement.getFormattedPath()));
                    return returnValue;
                }
                if(!(rawStatement instanceof TokenStructure)) continue;

                Statement statement = Statement.read(((TokenStructure) rawStatement).getContents(), this, function);

                if(statement != null) {
                    statement.setSilent(silent);
                    Value value = statement.writeToFunction(function);
                    if(statement instanceof ReturnStatement) {
                        returnValue = value;
                        closed = true;
                    }
                    //TEMPORARY. DO MORE STUFF OFC
                }
            }
        }

        return returnValue;
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
        return context.isStatic();
    }

    @Override
    public Context getParent() {
        return context;
    }

    @Override
    public SymbolTable getReferenceTable() {
        return null;
    }

    @Override
    public DataHolder getDataHolder() {
        return this;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return symbolTable;
    }

    @Override
    public MethodLog getMethodLog() {
        return this.isStatic() ? context.getUnit().getMethodLog() : context.getUnit().getGenericInstance().getMethodLog();
    }

    @Override
    public ObjectInstance getInstance() {
        return context.getInstance();
    }

    @Override
    public ScoreHolder getPlayer() {
        return (isStatic() ? context.getUnit().getPlayer() : context.getInstance().getScoreHolder());
    }
}
