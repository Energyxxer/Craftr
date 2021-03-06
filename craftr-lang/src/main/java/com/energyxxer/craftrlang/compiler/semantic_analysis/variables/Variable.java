package com.energyxxer.craftrlang.compiler.semantic_analysis.variables;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.Objective;
import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.codegen.objectives.LocalizedObjective;
import com.energyxxer.enxlex.pattern_matching.structures.TokenItem;
import com.energyxxer.enxlex.pattern_matching.structures.TokenList;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.enxlex.report.Notice;
import com.energyxxer.enxlex.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.InitContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.implicity.ImplicityState;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitValue;
import com.energyxxer.craftrlang.compiler.semantic_analysis.statements.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import com.energyxxer.util.Factory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator.ASSIGN;

/**
 * Created by Energyxxer on 07/10/2017.
 */
public class Variable extends ValueWrapper implements Symbol, DataHolder, TraversableStructure {
    private String constructorStuff = "";
    public final TokenPattern<?> pattern;

    private SymbolVisibility visibility;
    private List<CraftrLang.Modifier> modifiers;

    private DataType dataType;
    private String name;
    private VariableType type;

    private CodeBlock block = null;

    private Objective objective;

    private Value value;

    private ObjectInstance ownerInstance;

    private Factory<Value> lazyFactory;

    private Variable(Variable that, ObjectInstance ownerInstance, boolean empty) {
        super(that.semanticContext);
        this.pattern = that.pattern;
        this.visibility = that.visibility;
        this.modifiers = that.modifiers;
        this.dataType = that.dataType;
        this.name = that.name;
        this.type = that.type;
        this.block = that.block;
        //if(ownerInstance.isImplicit()) this.reference = new ScoreReference(new LocalScore(that.objective, ownerInstance.getEntity()));
        boolean a = false;
        if(empty) {
            this.reference = new ScoreReference(new LocalScore(that.objective, ownerInstance.requestEntity(null)));
            this.lazyFactory = () -> this.reference != null ? dataType.create(this.reference, this.semanticContext) : null;
        } else {
            this.lazyFactory = that.lazyFactory;
            this.value = that.value;
        }
        this.ownerInstance = ownerInstance;
        this.objective = that.objective;

        constructorStuff = "con0, " + that + ", " + ownerInstance + ", " + empty;
    }

    private Variable(TokenPattern<?> pattern, List<CraftrLang.Modifier> modifiers, DataType dataType, SemanticContext semanticContext) {
        super((semanticContext instanceof Unit && !modifiers.contains(CraftrLang.Modifier.STATIC)) ? ((Unit) semanticContext).getFieldInitContext() : semanticContext);
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
        this.type = (this.semanticContext.getContextType() == ContextType.UNIT) ? VariableType.FIELD : VariableType.VARIABLE;
        this.value = null;

        this.claimObjective();

        constructorStuff = "con1";
    }

    //FOR PARAMETER
    public Variable(String name, List<CraftrLang.Modifier> modifiers, DataType dataType, SemanticContext semanticContext, Value value, FunctionSection section, ScoreReference paramReference) {
        super(semanticContext);
        this.pattern = null;
        this.visibility = SymbolVisibility.METHOD;
        this.modifiers = new ArrayList<>(modifiers);
        this.dataType = dataType;
        this.name = name;
        //this.validName = !CraftrLang.isPseudoIdentifier(this.name);
        this.block = null;
        this.type = VariableType.PARAMETER;
        if(value != null) {
            if(value.isImplicit()) {
                this.reference = value.getReference().toScore(section, paramReference.getScore(), semanticContext); //Clone the value into the parameter objective
                this.value = dataType.create(this.reference, semanticContext);
            } else {
                this.value = value;
            }
        } else {
            this.reference = paramReference;
            this.value = dataType.create(this.reference, semanticContext); //Make a non-null value that points to the parameter objective. This shouldn't
        }

        this.claimObjective();

        constructorStuff = "con2";
    }

    @Deprecated
    private void claimObjective() {
        LocalizedObjective locObj = type.getGroup(semanticContext.getLocalizedObjectiveManager()).create();
        locObj.claim();
        //Never dispose
        this.objective = locObj.getObjective();
    }

    public void initializeValue() {
        if(this.pattern == null) return;
        TokenPattern<?> initialization = this.pattern.find("VARIABLE_INITIALIZATION");

        if(initialization != null) {

            FunctionSection initializerFunction;
            if(semanticContext instanceof Unit) {
                initializerFunction = ((Unit) semanticContext).getStaticInitializer();
            } else if(semanticContext instanceof InitContext) {
                initializerFunction = semanticContext.getUnit().getInstanceInitializer();
            } else if(semanticContext instanceof CodeBlock) {
                initializerFunction = ((CodeBlock) semanticContext).getFunctionSection();
            } else if(semanticContext instanceof Method) {
                return; //There's no variable initialization in method parameters;
                        // also this class isn't the one that parses method params so no point in having this;
            } else {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Something went wrong: Variable semanticContext is of unrecognized type: " + semanticContext.getClass().getSimpleName(), pattern));
                return;
            }

            this.reference = new ScoreReference(new LocalScore(objective, semanticContext.getScoreHolder(initializerFunction)));
            this.value = ExprResolver.analyzeValue(initialization.find("VALUE"), (semanticContext instanceof Unit && !isStatic()) ? ((Unit) semanticContext).getFieldInitContext() : semanticContext, null, initializerFunction);

            boolean a = false;

            if(this.value instanceof Expression) {
                this.value = ((Expression) this.value).unwrap(initializerFunction, getReference());
            }
            if(this.value != null && !this.value.getDataType().instanceOf(this.getDataType())) {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + this.value.getDataType() + " cannot be converted to " + this.dataType, initialization.find("VALUE")));
                this.value = null;
            }
            if(this.value == null) this.value = new Null(semanticContext);

            if(this.value instanceof ValueWrapper) {
                this.value = ((ValueWrapper) this.value).unwrap(initializerFunction);
            }

            if(!(this.value.getReference() instanceof ExplicitValue) || semanticContext.getUnit().getType().getImplicity() == ImplicityState.IMPLICIT) {
                ScoreReference asScoreReference = this.value.getReference().toScore(initializerFunction, getReference().getScore(), semanticContext);
                if(!(this.value.getReference() instanceof ExplicitValue)) this.value = dataType.create(asScoreReference, semanticContext);
            }

            //TODO: Debate whether explicit values should be assigned explicitly anyway after instantiation
            // Well depends on the unit type

            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Value Report", NoticeType.INFO, name + ": " + this.value, pattern));

            System.out.println(semanticContext);

            if(!isStatic() && semanticContext instanceof InitContext) {
                ((InitContext) semanticContext).updateVariable(this);
                System.out.println("UPDATED VARIABLE " + this);
            }
        } else {
            this.value = new Null(this.semanticContext);
        }

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
            variables.add(new Variable(p, modifiers, dataType, unit));
        }

        return variables;
    }

    private ScoreHolder requestParentScoreHolder(FunctionSection function) {
        if(isStatic()) {
            return getUnit().getScoreHolder(function);
        } else return ownerInstance.requestEntity(function);
    }

    public void createDataReference(FunctionSection function) {
        if(reference != null) return;
        reference = new ScoreReference(new LocalScore(objective, requestParentScoreHolder(function)));
    }

    public Value assign(Value value, FunctionSection function, SemanticContext semanticContext, TokenPattern<?> pattern, boolean silent) {
        if(!value.getDataType().instanceOf(this.getDataType())) {
            if(!silent) this.semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + value.getDataType() + " cannot be converted to " + this.getDataType(), pattern));
            this.value = new Null(this.semanticContext);
            return null;
        }

        if(value.isImplicit()) {
            createDataReference(function);
        }
        if(reference != null) {
            Value relocatedValue = value.getDataType().create(value.getReference().toScore(function, ((ScoreReference) reference).getScore(), semanticContext), semanticContext);
            if(value.isImplicit()) value = relocatedValue;
        }
        this.value = value;
        return value;
    }

    public Variable createNew(ObjectInstance ownerInstance) {
        return new Variable(this, ownerInstance, false);
    }

    public Variable createEmpty(ObjectInstance ownerInstance) {
        return new Variable(this, ownerInstance, true);
    }

    public Objective getObjective() {
        return objective;
    }

    private void lazilyInstantiateValue() {
        if(lazyFactory != null) value = lazyFactory.createInstance();
        lazyFactory = null;
    }

    public Value unwrap() {
        lazilyInstantiateValue();
        return value;
    }

    @Override
    public Value unwrap(FunctionSection section) {
        return unwrap();
    }

    public VariableType getType() {
        return type;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        switch(operator) {
            case ASSIGN: {
                return this.assign(operand, section, semanticContext, pattern, silent);
            }
            case ADD_THEN_ASSIGN:
            case SUBTRACT_THEN_ASSIGN:
            case MULTIPLY_THEN_ASSIGN:
            case DIVIDE_THEN_ASSIGN: {
                Value result;
                if(reference instanceof ScoreReference) {
                    result = unwrap().runShorthandOperation(reference, operator, operand, pattern, section, semanticContext, silent);
                } else {
                    result = unwrap().runOperation(Operator.getNoAssign(operator), operand, pattern, section, semanticContext, null, silent);
                }
                return (result != null) ? this.assign(result, section, semanticContext, pattern, silent) : null;
            }
        }
        lazilyInstantiateValue();
        if(!value.isNull()) {
            Value returnValue = value.runOperation(operator, operand, pattern, section, semanticContext, (operator == ASSIGN) ? this.getReference() : null, silent);
            return returnValue;
        } else {
            if(!silent) this.semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable might not have been defined", pattern));
            return value;
        }
    }

    @Override
    public ScoreReference getReference() {
        return (ScoreReference) reference;
    }

    @Override
    public boolean isExplicit() {
        lazilyInstantiateValue();
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
        lazilyInstantiateValue();
        return (value != null) ? value.getSubSymbolTable() : null;
    }

    @Override
    public MethodLog getMethodLog() {
        lazilyInstantiateValue();
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

    @Override
    public ObjectInstance asObjectInstance() {
        lazilyInstantiateValue();
        return (value != null && value instanceof ObjectInstance) ? (ObjectInstance) value : null;
    }

    @Override
    public String toString() {
        return type.toString().toLowerCase() + " '" + name + "': " + semanticContext;
    }
}
