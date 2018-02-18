package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.FakePlayer;
import com.energyxxer.commodore.score.ScoreHolder;
import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodSignature;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;
import com.energyxxer.util.out.Console;
import com.energyxxer.util.vprimitives.VInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class Unit extends AbstractFileComponent implements Symbol, DataHolder, Context, TraversableStructure {
    /**
     * The <code>CraftrFile</code> that declares this unit.
     * */
    private final CraftrFile declaringFile;
    /**
     * The context used for instance things...? I don't think we even need this.
     * */
    private final Context instanceContext;
    /**
     * The visibility of this unit. Either:
     * <ul>
     *     <li><code>SymbolVisibility.GLOBAL</code></li>
     *     <li><code>SymbolVisibility.PACKAGE</code></li>
     * </ul>
     * Other types of visibility are deemed illegal.
     * */
    private final SymbolVisibility visibility;
    /**
     * The simple name of this unit.
     * */
    private final String name;
    /**
     * This unit's type. Either:
     * <ul>
     *     <li><code>UnitType.ENTITY</code></li>
     *     <li><code>UnitType.ITEM</code></li>
     *     <li><code>UnitType.FEATURE</code></li>
     *     <li><code>UnitType.CLASS</code></li>
     *     <li><code>UnitType.ENUM</code></li>
     *     <li><code>UnitType.WORLD</code></li>
     * </ul>
     * */
    private final UnitType type;

    /**
     * Unit that this unit immediately extends.
     * <br>
     * This may or may not be valid and could reflect cyclical inheritance.
     * */
    private Unit superUnit = null;
    /**
     * A list of other units that this unit inherits.
     * <br><br>
     * Contains features in the order of the implementation list, if any
     * <br>
     * and then a list of all the units this recursively extends,
     * <br>
     * from the highest-level superunit to <code>craftr.lang.Object</code>.
     * <br><br>
     * This may be null until compilation stage 3.
     * */
    private List<Unit> inheritanceMap = null;
    /**
     * A list of all features this unit implements,
     * <br>
     * in the order of the implementation list.
     * <br><br>
     *     This may be null until compilation stage 2.
     * */
    private List<Unit> features = null;
    /**
     * The units this feature requires in order to be implemented,
     * <br>
     * in the order of the implementation list.
     * <br><br>
     *     This may be null until compilation stage 2.
     * */
    private Unit requirement = null;

    /**
     * The flat list of tokens that make up the reference to the extended unit.
     * <br><br>
     *     This may be null if there is no explicit extension.
     * */
    private List<Token> rawUnitExtends = null;
    /**
     * A list of the flat lists of tokens that make up the references to the implemented units.
     * <br><br>
     *     This may be null if there is no explicit implementation.
     * */
    private List<List<Token>> rawUnitImplements = null;
    /**
     * The flat list of tokens that make up the reference to the required unit.
     * <br><br>
     *     This may be null if there is no explicit requirement.
     * */
    private List<Token> rawUnitRequires = null;

    /**
     * The generic instance for this unit, used for non-static analysis.
     * <br><br>
     *     This may be null until compilation stage 5.
     * */
    private ObjectInstance genericInstance = null;
    /**
     * The data type that represents this unit.
     * */
    private DataType dataType;

    /**
     * The field log containing static fields.
     * */
    private FieldLog staticFieldLog;
    /**
     * The field log containing non-static fields.
     * */
    private FieldLog instanceFieldLog;
    /**
     * The field log containing static methods.
     * */
    private MethodLog staticMethodLog;
    /**
     * The field log containing non-static methods.
     * */
    private MethodLog instanceMethodLog;

    /**
     * False until compilation stage 2.
     * */
    private boolean unitActionsInitialized = false;
    /**
     * False until compilation stage 5.
     * */
    private boolean unitComponentsInitialized = false;

    /**
     * Function responsible for static initialization of this unit.
     * */
    private Function staticInitializer;
    /**
     * Function responsible for instantiation of objects of this unit.
     * */
    private Function instanceInitializer;

    /**
     * The numeric ID of this unit type.
     * <br><br>
     *     -1 until compilation stage 4.
     * */
    private int unitID = -1;

    private FakePlayer staticPlayer;

    public Unit(CraftrFile file, TokenPattern<?> pattern) {
        super(pattern);
        this.declaringFile = file;

        //Parse header

        TokenPattern<?> header = pattern.find("UNIT_DECLARATION");

        this.name = ((TokenItem) header.find("UNIT_NAME")).getContents().value;
        String rawType = ((TokenItem) header.find("UNIT_TYPE")).getContents().value.toUpperCase();
        this.type = UnitType.valueOf(rawType);

        if(this.type == UnitType.ENTITY && !Character.isLowerCase(name.charAt(0))) declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Entity name '" + this.name + "' does not follow Craftr naming conventions", header.find("UNIT_NAME").getFormattedPath()));

        List<CraftrLang.Modifier> modifiers = SemanticUtils.getModifiers(header.deepSearchByName("UNIT_MODIFIER"), file.getAnalyzer());

        List<TokenPattern<?>> actionPatterns = header.deepSearchByName("UNIT_ACTION");
        for(TokenPattern<?> p : actionPatterns) {
            String actionType = ((TokenItem) p.find("UNIT_ACTION_TYPE")).getContents().value;
            switch(actionType) {
                case "extends": {
                    if(rawUnitExtends != null) {
                        file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate unit action 'extends'", p.getFormattedPath()));
                        break;
                    }

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    if(references.size() > 1) {
                        file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unit cannot extend multiple units", p.getFormattedPath()));
                    }

                    rawUnitExtends = references.get(0).flattenTokens();
                    break;
                }
                case "implements": {
                    if(rawUnitImplements != null) {
                        file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate unit action 'implements'", p.getFormattedPath()));
                        break;
                    }
                    rawUnitImplements = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        List<Token> flat = reference.flattenTokens();
                        if(!rawUnitImplements.contains(flat)) rawUnitImplements.add(flat);
                        else file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate unit '" + reference.flatten(false) + "'", p.getFormattedPath()));
                    }
                    break;
                }
                case "requires": {
                    if(rawUnitRequires != null) {
                        file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate unit action 'requires'", p.getFormattedPath()));
                        break;
                    }

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    if(references.size() > 1) {
                        file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unit cannot require multiple units", p.getFormattedPath()));
                    }

                    rawUnitRequires = references.get(0).flattenTokens();
                    break;
                }
                default: {
                    Console.err.println("[ERROR] Unrecognized unit action \"" + actionType + "\"");
                }
            }
        }

        this.visibility = modifiers.contains(CraftrLang.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL : SymbolVisibility.PACKAGE;

        file.getPackage().getSubSymbolTable().put(this);

        this.staticFieldLog = new FieldLog(this);
        this.instanceFieldLog = new FieldLog(this);

        this.staticMethodLog = new MethodLog(this);
        this.instanceMethodLog = new MethodLog(this);

        this.instanceContext = new Context() {
            @Override
            public CraftrFile getDeclaringFile() {
                return file;
            }

            @Override
            public Unit getUnit() {
                return Unit.this;
            }

            @Override
            public ContextType getContextType() {
                return ContextType.UNIT;
            }

            @Override
            public SemanticAnalyzer getAnalyzer() {
                return Unit.this.getAnalyzer();
            }

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public Context getParent() {
                return Unit.this;
            }

            @Override
            public SymbolTable getReferenceTable() {
                return null; //WHAT TO DO
            }

            @Override
            public DataHolder getDataHolder() {
                return genericInstance;
            }

            @Override
            public ObjectInstance getInstance() {
                return genericInstance;
            }

            @Override
            public ScoreHolder getPlayer() {
                return genericInstance.getScoreHolder();
            }
        };

        //staticPlayer = getAnalyzer().getCompiler().getDataPackBuilder().getScoreHolderManager().createFakePlayer(this.name.toUpperCase());

        dataType = new DataType(this);
        dataType.setReferenceConstructor((r,c) -> new ObjectInstance(this, r, c));

        staticInitializer = this.getModuleNamespace().getFunctionManager().create(this.getFunctionPath() + "/$initStatic");
        instanceInitializer = this.getModuleNamespace().getFunctionManager().create(this.getFunctionPath() + "/$init");
    }

    void initUnitActions() {
        if(unitActionsInitialized) return;

        if(rawUnitExtends != null) {
            Symbol symbol = declaringFile.getReferenceTable().getSymbol(rawUnitExtends, this);
            if(symbol != null) {
                if(symbol instanceof Unit && ((Unit) symbol).type == this.type) {
                    superUnit = (Unit) symbol;
                } else {
                    declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, this.type.getName() + " name expected", rawUnitExtends.get(rawUnitExtends.size()-1).getFormattedPath()));
                }
            }
        }

        if(superUnit == null) {
            if(!this.getFullyQualifiedName().equals("craftr.lang.Object")) {
                if(type == UnitType.ENTITY && !this.getFullyQualifiedName().equals("craftr.lang.entities.entity_base")) {
                    superUnit = (Unit)
                            getAnalyzer().getLangPackage()
                                    .getSubSymbolTable()
                                    .getMap()
                                    .get("entities")
                                    .getSubSymbolTable()
                                    .getMap()
                                    .get("entity_base");
                } else if(type == UnitType.ENUM && !this.getFullyQualifiedName().equals("craftr.lang.Enum")) {
                    superUnit = (Unit)
                            getAnalyzer()
                                    .getLangPackage()
                                    .getSubSymbolTable()
                                    .getMap()
                                    .get("Enum");
                } else if(type == UnitType.WORLD && !this.getFullyQualifiedName().equals("craftr.lang.World")) {
                    superUnit = (Unit)
                            getAnalyzer().getLangPackage()
                                    .getSubSymbolTable()
                                    .getMap()
                                    .get("World");
                } else {
                    superUnit = (Unit)
                            getAnalyzer().getLangPackage()
                                    .getSubSymbolTable()
                                    .getMap()
                                    .get("Object");
                }
            }


        }

        this.features = new ArrayList<>();

        if(rawUnitImplements != null) {
            for(List<Token> path : rawUnitImplements) {
                Symbol symbol = declaringFile.getReferenceTable().getSymbol(path, this);
                if(symbol != null) {
                    if(symbol instanceof Unit && ((Unit) symbol).type == UnitType.FEATURE) {
                        this.features.add((Unit) symbol);
                    } else {
                        declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, UnitType.FEATURE.getName() + " name expected", path.get(path.size()-1).getFormattedPath()));
                    }
                }
            }
        }

        if(rawUnitRequires != null) {
            if(this.type != UnitType.FEATURE) {
                declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "'requires' action is only valid for " + UnitType.FEATURE.getName() + " units", rawUnitRequires.get(rawUnitRequires.size()-1).getFormattedPath()));
            } else {
                Symbol symbol = declaringFile.getReferenceTable().getSymbol(rawUnitRequires, this);
                if(symbol != null) {
                    if(symbol instanceof Unit) {
                        if(((Unit) symbol).type != UnitType.FEATURE) {
                            this.requirement = (Unit) symbol;
                        } else {
                            declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Non-" + UnitType.FEATURE.getName().toLowerCase() + " unit name expected", rawUnitRequires.get(rawUnitRequires.size() - 1).getFormattedPath()));
                        }
                    } else {
                        declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unit name expected", rawUnitRequires.get(rawUnitRequires.size()-1).getFormattedPath()));
                    }
                }
            }
        }

        unitActionsInitialized = true;
    }

    void buildInheritanceMap() {
        if(inheritanceMap != null || !unitActionsInitialized) return;
        inheritanceMap = new ArrayList<>();
        inheritanceMap.addAll(features);

        ArrayList<Unit> knownParents = new ArrayList<>();
        Unit current = this;
        while(current != null) {
            if(knownParents.contains(current)) {
                declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cyclic inheritance involving '" + current.getFullyQualifiedName() + "'", this.pattern.find("UNIT_DECLARATION").getFormattedPath()));
                break;
            } else {
                knownParents.add(current);
                if(current != this) inheritanceMap.add(current);
            }
            current = current.superUnit;
        }
    }

    void resetUnitID() {
        Unit trueSuperUnit = null;
        for(Unit u : inheritanceMap) {
            if(u.type != UnitType.FEATURE) {
                trueSuperUnit = u;
                break;
            }
        }
        if(trueSuperUnit != null) trueSuperUnit.resetUnitID();

        if(this.unitID >= 0) {
            getAnalyzer().getCompiler().getReport().addNotice(new Notice("ID Reset",NoticeType.INFO,this.getFullyQualifiedName() + " reset"));
        }
        this.unitID = -1;
    }

    void assignUnitID(VInteger id) {
        if(this.unitID >= 0) return;
        Unit trueSuperUnit = null;
        for(Unit u : inheritanceMap) {
            if(u.type != UnitType.FEATURE) {
                trueSuperUnit = u;
                break;
            }
        }
        if(trueSuperUnit != null) trueSuperUnit.assignUnitID(id);
        this.unitID = id.value;
        id.value++;

        getAnalyzer().getCompiler().getReport().addNotice(new Notice("Unit IDs",NoticeType.INFO, this.getFullyQualifiedName() + "#" + this.unitID));
    }

    void checkActionCompatibility() {
        HashMap<MethodSignature, Method> allMethods = new HashMap<>();

        for(Unit inherited : inheritanceMap) {
            for(Method method : inherited.getAllMethods()) {
                Method clashingMethod = allMethods.get(method.getSignature());
                if(clashingMethod != null) {
                    if(!clashingMethod.getReturnType().equals(method.getReturnType())) {
                        declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(
                                NoticeType.ERROR,
                                "'" + method.getSignature() + "' in '" + method.getUnit().getFullyQualifiedName() +
                                        "' clashes with '" + clashingMethod.getSignature() + "' in '" +
                                        clashingMethod.getUnit().getFullyQualifiedName() +
                                        "'; attempting to use incompatible return type", this.pattern.find("UNIT_DECLARATION").getFormattedPath()));
                    }
                } else allMethods.put(method.getSignature(), method);
            }
        }
    }

    void initUnitComponents() {
        if(unitComponentsInitialized) return;
        TokenPattern<?> componentList = (type == UnitType.ENUM) ? pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST_WRAPPER.UNIT_COMPONENT_LIST") : pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST");
        if(componentList != null) {
            for (TokenPattern<?> p : componentList.searchByName("UNIT_COMPONENT")) {
                TokenStructure component = (TokenStructure) p.getContents();
                if (component.getName().equals("VARIABLE")) {
                    List<Variable> newFields = Variable.parseDeclaration(component, this);
                    for(Variable field : newFields) {
                        if(!staticFieldLog.getMap().containsKey(field.getName()) && !instanceFieldLog.getMap().containsKey(field.getName())) {
                            ((field.isStatic()) ? staticFieldLog : instanceFieldLog).addField(field);
                        } else {
                            getAnalyzer().getCompiler().getReport().addNotice(new Notice(
                                    NoticeType.ERROR,
                                    "Variable '" + name + "' already declared in the scope",
                                    field.pattern.find("VARIABLE_NAME").getFormattedPath()
                            ));
                        }
                    }
                } else if(component.getName().equals("METHOD")) {
                    Method method = new Method(this, component);
                    if(staticMethodLog.findMethod(method.getSignature()) == null && instanceMethodLog.findMethod(method.getSignature()) == null) {
                        ((method.isStatic()) ? staticMethodLog : instanceMethodLog).addMethod(method);
                    } else {
                        getAnalyzer().getCompiler().getReport().addNotice(new Notice(
                                NoticeType.ERROR,
                                "'" + method.getSignature() + "' is already defined in '" + this.getFullyQualifiedName() + "'",
                                component.getFormattedPath()
                        ));
                    }
                }
            }
        }

        this.genericInstance = new ObjectInstance(this, this.instanceContext);

        staticFieldLog.forEachVar(Variable::initializeValue);
        instanceFieldLog.forEachVar(Variable::initializeValue);

        unitComponentsInitialized = true;
    }

    void initCodeBlocks() {
        staticMethodLog.initCodeBlocks();
        instanceMethodLog.initCodeBlocks();
    }

    @Override
    public DataHolder getDataHolder() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SymbolVisibility getVisibility() {
        return visibility;
    }

    @Override
    public @NotNull Package getPackage() {
        return declaringFile.getPackage();
    }

    @Override
    public ContextType getContextType() {
        return ContextType.UNIT;
    }

    @Override
    public @NotNull SymbolTable getSubSymbolTable() {
        return staticFieldLog;
    }

    @Override
    public @NotNull CraftrFile getDeclaringFile() {
        return declaringFile;
    }

    @Override
    public @Nullable Unit getUnit() {
        return this;
    }

    @Override
    public @NotNull SemanticAnalyzer getAnalyzer() {
        return declaringFile.getAnalyzer();
    }

    public String getFullyQualifiedName() {
        return declaringFile.getPackage().getFullyQualifiedName() + "." + name;
    }

    public String getFunctionPath() {
        return getFullyQualifiedName().replace("\\.","/");
    }

    public FieldLog getStaticFieldLog() {
        return staticFieldLog;
    }

    public FieldLog getInstanceFieldLog() {
        return instanceFieldLog;
    }

    public MethodLog getStaticMethodLog() {
        return staticMethodLog;
    }

    public MethodLog getInstanceMethodLog() {
        return instanceMethodLog;
    }

    public Function getStaticInitializer() {
        return staticInitializer;
    }

    public Function getInstanceInitializer() {
        return instanceInitializer;
    }

    public boolean instanceOf(Unit unit) {
        if(this == unit) return true;
        for(Unit inherited : inheritanceMap) {
            if(inherited == unit) return true;
        }
        return false;
    }

    public List<Method> getAllMethods() {
        ArrayList<Method> all = new ArrayList<>();
        all.addAll(staticMethodLog.getAllMethods());
        all.addAll(instanceMethodLog.getAllMethods());
        return all;
    }

    public UnitType getType() {
        return type;
    }

    @Override
    public MethodLog getMethodLog() {
        return staticMethodLog;
    }

    public ObjectInstance getGenericInstance() {
        return genericInstance;
    }

    @Override
    public ObjectInstance getInstance() {
        return null;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public Context getInstanceContext() {
        return this.instanceContext;
    }

    public List<Unit> getInheritanceMap() {
        return inheritanceMap;
    }

    @Override
    public Context getParent() {
        return declaringFile;
    }

    @Override
    public SymbolTable getReferenceTable() {
        return null;
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public ScoreHolder getPlayer() {
        return staticPlayer;
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
        /*return "" + modifiers + " " + type + " " + name + ""
                + ((rawUnitExtends != null) ? " extends " + rawUnitExtends: "")
                + ((rawUnitImplements != null) ? " implements " + rawUnitImplements: "")
                + ((rawUnitRequires != null) ? " requires " + rawUnitRequires: "");*/
    }
}