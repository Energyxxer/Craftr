package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class CodeBlock extends Statement implements SemanticContext, DataHolder {
    private boolean closed = false;

    private LocalizedObjectiveManager locObjMgr;

    private ObjectInstance ownerInstance;

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
            else {
                if(ownerInstance == null) throw new IllegalArgumentException("Code block's initialize method hasn't been called yet, cannot get symbols");
                return ownerInstance.getSubSymbolTable().getSymbol(flatTokens, semanticContext, silent || CodeBlock.this.silent);
            }
        }
    };

    private ArrayList<Statement> statements;

    private boolean initialized = false;

    private static Function createConstructorFunction(SemanticContext semanticContext) {
        if(semanticContext instanceof Method) {
            return ((Method) semanticContext).getFunction();
        } else {
            String name;
            if(semanticContext instanceof CodeBlock && ((CodeBlock) semanticContext).section instanceof Function) {
                name = ((Function) ((CodeBlock) semanticContext).section).getFullName();
                name = name.substring(name.indexOf(":")+1);
            } else {
                name = semanticContext.getUnit().getFunctionPath() + "/cbk-" + semanticContext.getDeclaringFile().getIOFile().getName();
            }
            return semanticContext.getModuleNamespace().getFunctionManager().create(name, true);
        }
    }

    public CodeBlock(TokenPattern<?> pattern, SemanticContext semanticContext) {
        super(pattern, semanticContext, createConstructorFunction(semanticContext));

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

    public void initialize(ObjectInstance ownerInstance) {
        if(initialized) return;

        this.ownerInstance = (ownerInstance != null) ? ownerInstance : new ObjectInstance(semanticContext.getUnit(), this, false);
        //semanticContext.getUnit().getDataType().create(null, this);

        TokenPattern<?> inner = (TokenPattern<?>) pattern.getContents();

        closed = false;

        statements = new ArrayList<>();

        TokenList list = (TokenList) inner.find("STATEMENT_LIST");
        if(list != null) {
            TokenPattern<?>[] rawStatements = list.getContents();
            for(TokenPattern<?> rawStatement : rawStatements) {
                if(closed) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unreachable statement", rawStatement));
                    return;
                }
                if(!(rawStatement instanceof TokenStructure)) continue;

                Statement statement = Statement.read(((TokenStructure) rawStatement).getContents(), this, section);

                if(statement != null) {
                    statements.add(statement);
                    statement.setSilent(silent);
                    Value value = statement.evaluate(section);
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

    @Override
    public boolean isExplicit() {
        for(Statement statement : statements) {
            if(!statement.isExplicit()) return false;
        }
        return true;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public FunctionSection getFunctionSection() {
        return section;
    }

    public int getLevel() {
        return (parentBlock != null) ? parentBlock.getLevel()+1 : 0;
    }

    @Override
    public Value evaluate(FunctionSection section) {
        System.out.println("EVALUATING CODE BLOCK IN FUNCTION " + section);
        for(Statement st : statements) {
            st.setDataHolder(this);
            Value value = st.evaluate(section);
            if(st instanceof ReturnStatement) return value;
        }
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
    public DataHolder getDataHolder() {
        return this;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return symbolTable;
    }

    @Override
    public MethodLog getMethodLog() {
        if(isStatic()) {
            return semanticContext.getUnit().getMethodLog();
        } else {
            if(ownerInstance == null) throw new IllegalStateException("Code block's initialize method hasn't been called yet, cannot get method log");
            return ownerInstance.getMethodLog();
        }
    }

    @Override
    public ObjectInstance getInstance() {
        return semanticContext.getInstance();
    }

    @Override
    public LocalizedObjectiveManager getLocalizedObjectiveManager() {
        return locObjMgr;
    }

    @Override
    public ScoreHolder getScoreHolder(FunctionSection section) {
        return (isStatic() ? semanticContext.getUnit().getScoreHolder(section) : ownerInstance.getEntity());
    }

    @Override
    public ObjectInstance asObjectInstance() {
        return ownerInstance;
    }
}
