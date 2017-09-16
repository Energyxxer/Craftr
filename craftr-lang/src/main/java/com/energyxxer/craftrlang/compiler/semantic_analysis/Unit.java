package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodSignature;
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
public class Unit extends AbstractFileComponent implements Symbol, Context {
    private final CraftrFile declaringFile;
    private final Context instanceContext;
    private List<CraftrUtil.Modifier> modifiers;
    private final SymbolVisibility visibility;
    private final String name;
    private final UnitType type;

    private Unit superUnit = null;
    private List<Unit> inheritanceMap = null;
    private List<Unit> features = null;

    private List<Token> rawUnitExtends = null;
    private List<List<Token>> rawUnitImplements = null;
    private List<List<Token>> rawUnitRequires = null;

    private FieldManager fieldManager;
    private MethodManager methodManager;

    private boolean unitActionsInitialized = false;
    private boolean unitComponentsInitialized = false;

    private int unitID = -1;

    public Unit(CraftrFile file, TokenPattern<?> pattern) {
        super(pattern);
        this.declaringFile = file;

        //Parse header

        TokenPattern<?> header = pattern.find("UNIT_DECLARATION");

        this.name = ((TokenItem) header.find("UNIT_NAME")).getContents().value;
        String rawType = ((TokenItem) header.find("UNIT_TYPE")).getContents().value.toUpperCase();
        this.type = UnitType.valueOf(rawType);

        if(this.type == UnitType.ENTITY && !Character.isLowerCase(name.charAt(0))) declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Entity name '" + this.name + "' does not follow Craftr naming conventions", header.find("UNIT_NAME").getFormattedPath()));

        this.modifiers = SemanticUtils.getModifiers(header.deepSearchByName("UNIT_MODIFIER"), file.getAnalyzer());

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
                    rawUnitRequires = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        List<Token> flat = reference.flattenTokens();
                        if(!rawUnitRequires.contains(flat)) rawUnitRequires.add(flat);
                        else file.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate unit '" + reference.flatten(false) + "'", p.getFormattedPath()));
                    }
                    break;
                }
                default: {
                    Console.err.println("[ERROR] Unrecognized unit action \"" + actionType + "\"");
                }
            }
        }

        this.visibility = modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL : SymbolVisibility.PACKAGE;

        file.getPackage().getSubSymbolTable().put(this);

        this.fieldManager = new FieldManager(this);
        this.methodManager = new MethodManager(this);

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
        };
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
            if(!this.getFullyQualifiedName().equals("craftr.lang.Object")) superUnit = (Unit) declaringFile.getReferenceTable().getMap().get("craftr").getSubSymbolTable().getMap().get("lang").getSubSymbolTable().getMap().get("Object");
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
            for(Method method : inherited.methodManager.getAllMethods()) {
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
                    fieldManager.insertField(component);
                } else if(component.getName().equals("METHOD")) {
                    methodManager.insertMethod(component);
                }
            }
        }
        unitComponentsInitialized = true;
    }

    void initCodeBlocks() {
        methodManager.initCodeBlocks();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SymbolVisibility getVisibility() {
        return modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL : SymbolVisibility.PACKAGE;
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
        return fieldManager.getStaticFieldTable();
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

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    public MethodManager getMethodManager() {
        return methodManager;
    }

    public boolean instanceOf(Unit unit) {
        if(this == unit) return true;
        for(Unit inherited : inheritanceMap) {
            if(inherited == unit) return true;
        }
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    public Context getInstanceContext() {
        return this.instanceContext;
    }

    @Override
    public String toString() {
        return name;
        /*return "" + modifiers + " " + type + " " + name + ""
                + ((rawUnitExtends != null) ? " extends " + rawUnitExtends: "")
                + ((rawUnitImplements != null) ? " implements " + rawUnitImplements: "")
                + ((rawUnitRequires != null) ? " requires " + rawUnitRequires: "");*/
    }
}