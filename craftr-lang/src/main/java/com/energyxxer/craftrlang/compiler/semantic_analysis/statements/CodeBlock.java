package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.commands.function.FunctionCommand;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjectiveManager;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
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
public class CodeBlock extends Statement implements SemanticContext, DataHolder {
    private boolean closed = false;

    private LocalizedObjectiveManager locObjMgr;

    private CodeBlock parentBlock = null;
    private SymbolTable symbolTable = new SymbolTable() {
        @Override
        public Symbol getSymbol(List<Token> flatTokens, SemanticContext semanticContext, boolean silent) {
            if(flatTokens.size() > 1) {
                //I don't think this should even be allowed
                if(!silent && !CodeBlock.this.silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Trying to get a symbol of more than one token from a code block...?", flatTokens.get(0)));
                return super.getSymbol(flatTokens, semanticContext, silent);
            }

            Symbol sym = super.getSymbol(flatTokens, semanticContext, true);
            if(sym != null) return sym;
            if(parentBlock != null) return parentBlock.getSymbolTable().getSymbol(flatTokens, semanticContext, silent || CodeBlock.this.silent);
            if(isStatic()) return semanticContext.getUnit().getStaticFieldLog().getSymbol(flatTokens, semanticContext, silent || CodeBlock.this.silent);
            else return semanticContext.getUnit().getGenericInstance().getSubSymbolTable().getSymbol(flatTokens, semanticContext, silent || CodeBlock.this.silent);
        }
    };

    private boolean initialized = false;

    public CodeBlock(TokenPattern<?> pattern, SemanticContext semanticContext) {
        super(pattern, semanticContext, (semanticContext instanceof Method) ? ((Method) semanticContext).getFunction() : semanticContext.getModuleNamespace().getFunctionManager().create(
                ((semanticContext instanceof CodeBlock) ?
                        ((CodeBlock) semanticContext).function.getFullName()
                        :
                        semanticContext.getUnit().getFunctionPath() + "/cbk@" + semanticContext.getDeclaringFile().getIOFile().getName()
                ), true
        ));

        if(semanticContext instanceof CodeBlock) {
            this.parentBlock = (CodeBlock) semanticContext;
            this.locObjMgr = parentBlock.locObjMgr;
        }

        if(semanticContext instanceof CodeBlock || semanticContext instanceof Method) {
            this.locObjMgr = semanticContext.getLocalizedObjectiveManager();
        } else {
            this.locObjMgr = getAnalyzer().getCompiler().getModule().createLocalizedObjectiveManager(this);
        }

        this.clearSymbols();
    }

    public void clearSymbols() {
        this.symbolTable.clear();
    }

    public void initialize() {
        if(initialized) return;
        TokenPattern<?> inner = (TokenPattern<?>) pattern.getContents();

        closed = false;

        TokenList list = (TokenList) inner.find("STATEMENT_LIST");
        if(list != null) {
            TokenPattern<?>[] rawStatements = list.getContents();
            for(TokenPattern<?> rawStatement : rawStatements) {
                if(closed) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unreachable statement", rawStatement));
                    return;
                }
                if(!(rawStatement instanceof TokenStructure)) continue;

                Statement statement = Statement.read(((TokenStructure) rawStatement).getContents(), this, function);

                if(statement != null) {
                    statement.setSilent(silent);
                    Value value = statement.evaluate(function);
                    if(statement instanceof ReturnStatement) {
                        //returnValue = value;
                        closed = true;
                    }
                    //TEMPORARY. DO MORE STUFF OFC
                }
            }
        }
        initialized = true;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Function getFunction() {
        return function;
    }

    public int getLevel() {
        return (parentBlock != null) ? parentBlock.getLevel()+1 : 0;
    }

    @Override
    public Value evaluate(Function function) {
        function.append(new FunctionCommand(this.function));
        return null;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.BLOCK;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return semanticContext.getDeclaringFile();
    }

    @Override
    public Unit getUnit() {
        return semanticContext.getUnit();
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return semanticContext.getAnalyzer();
    }

    @Override
    public boolean isStatic() {
        return semanticContext.isStatic();
    }

    @Override
    public SemanticContext getParent() {
        return semanticContext;
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
        return this.isStatic() ? semanticContext.getUnit().getMethodLog() : semanticContext.getUnit().getGenericInstance().getMethodLog();
    }

    @Override
    public ObjectInstance getInstance() {
        return semanticContext.getInstance();
    }

    @Override
    public LocalizedObjectiveManager getLocalizedObjectiveManager() {
        return null;
    }

    @Override
    public ScoreHolder getPlayer() {
        return (isStatic() ? semanticContext.getUnit().getPlayer() : semanticContext.getInstance().getEntity());
    }
}
