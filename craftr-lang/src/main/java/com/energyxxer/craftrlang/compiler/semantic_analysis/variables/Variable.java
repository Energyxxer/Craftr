package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.UnitInstanceContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
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

    private SymbolVisibility visibility;
    private List<CraftrLang.Modifier> modifiers;

    private DataType dataType;
    private String name;
    private boolean field = false;

    private CodeBlock block = null;
    private Method method = null;

    private Objective objective;

    private Value value;
    private ScoreReference reference = null;

    private Variable(Variable that) {
        super(that.semanticContext);
        this.pattern = that.pattern;
        this.visibility = that.visibility;
        this.modifiers = that.modifiers;
        this.dataType = that.dataType;
        this.name = that.name;
        this.field = that.field;
        this.block = that.block;
        this.method = that.method;
        this.value = dataType.create(that.reference, this.semanticContext);
        this.objective = that.objective;
        this.reference = that.reference;
    }

    private Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, SemanticContext semanticContext) {
        super((semanticContext instanceof Unit) ? ((Unit) semanticContext).getInstanceSemanticContext() : semanticContext);
        this.pattern = pattern;
        this.modifiers = new ArrayList<>(modifiers);
        this.dataType = dataType;

        this.visibility = (
                (modifiers.contains(CraftrLang.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrLang.Modifier.PROTECTED) ? SymbolVisibility.UNIT_INHERITED :
                modifiers.contains(CraftrLang.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                this.semanticContext instanceof CodeBlock ? SymbolVisibility.BLOCK : SymbolVisibility.PACKAGE));

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;

        if(CraftrLang.isPseudoIdentifier(this.name)) {
            this.semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal variable name", pattern.find("VARIABLE_NAME")));
        }
        this.value = null;
        this.field = this.semanticContext.getContextType() == ContextType.UNIT;

        this.claimObjective();
    }

    public Variable(String name, List<CraftrLang.Modifier> modifiers, DataType dataType, Method method, Value value, Function function) {
        super(method);
        this.pattern = null;
        this.visibility = SymbolVisibility.METHOD;
        this.modifiers = new ArrayList<>(modifiers);
        this.dataType = dataType;
        this.name = name;
        //this.validName = !CraftrLang.isPseudoIdentifier(this.name);
        this.block = null;
        this.method = method;
        this.claimObjective(); //Claim the parameter objective
        this.updateReference();
        this.reference = value.getReference().toScore(function, this.reference.getScore(), semanticContext); //Clone the value into the parameter objective
        this.value = dataType.create(this.reference, semanticContext); //Make a non-null value that points to the parameter objective. This shouldn't

        this.claimObjective();
    }

    private void claimObjective() {
        LocalizedObjective locObj = (
                field ?
                        semanticContext.getLocalizedObjectiveManager().FIELD :
                        (semanticContext instanceof Method ?
                                semanticContext.getLocalizedObjectiveManager().PARAMETER :
                                semanticContext.getLocalizedObjectiveManager().VARIABLE)).create();
        locObj.capture();
        //Never dispose
        this.objective = locObj.getObjective();
    }

    private void updateReference() {
        this.reference = new ScoreReference(new LocalScore(objective, semanticContext.getPlayer()));
    }

    public void initializeValue() {
        if(this.pattern == null) return;
        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {

            Function initializerFunction;
            if(semanticContext instanceof Unit) {
                initializerFunction = ((Unit) semanticContext).getStaticInitializer();
            } else if(semanticContext instanceof UnitInstanceContext) {
                initializerFunction = semanticContext.getUnit().getInstanceInitializer();
            } else if(semanticContext instanceof CodeBlock) {
                initializerFunction = ((CodeBlock) semanticContext).getFunction();
            } else if(semanticContext instanceof Method) {
                return; //There's no variable initialization in method parameters;
                        // also this class isn't the one that parses method params so no point in having this;
            } else {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Something went wrong: Variable semanticContext is of unrecognized type: " + semanticContext.getClass().getSimpleName(), pattern));
                return;
            }

            this.value = ExprResolver.analyzeValue(initialization.find("VALUE"), (semanticContext instanceof Unit && !isStatic()) ? ((Unit) semanticContext).getInstanceSemanticContext() : semanticContext, null, initializerFunction);
            if(this.value != null) {
                this.value = this.value.unwrap(initializerFunction);
            }
            if(this.value != null && !this.value.getDataType().instanceOf(this.getDataType())) {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE")));
                this.value = null;
            }
            if(this.value == null) this.value = new Null(semanticContext);
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report", NoticeType.INFO, name + ": " + this.value, pattern));
        } else {
            this.value = new Null(this.semanticContext);
        }

        reference = new ScoreReference(new LocalScore(objective, semanticContext.getPlayer()));
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
            variables.add(new Variable(p, modifiers, dataType, (SemanticContext) unit));
        }

        return variables;
    }

    public Variable duplicate() {
        return new Variable(this);
    }

    public Objective getObjective() {
        return objective;
    }

    public Value getValue() {
        return value;
    }

    public boolean isField() {
        return field;
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
        switch(operator) {
            case ASSIGN: {
                if(operand.getDataType().instanceOf(this.getDataType())) {
                    this.value = operand;

                    if(!operand.isNull() && operand.getReference() != null) {
                        this.reference = operand.getReference().toScore(function, new LocalScore(objective, semanticContext.getPlayer()), semanticContext);
                    } else {
                        semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Assigned value may not have been initialized", pattern));
                    }
                } else {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern));
                    this.value = new Null(semanticContext);
                }
                return value;
            }
        }
        if(!value.isNull()) return value.runOperation(operator, operand, pattern, function, silent);
        else {
            if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable might not have been defined", pattern));
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
    public Unit getUnit() {
        return semanticContext.getUnit();
    }

    @Override
    public Value clone(Function function) {
        throw new IllegalStateException("Dude, don't clone the variable, clone the value!");
    }
}
