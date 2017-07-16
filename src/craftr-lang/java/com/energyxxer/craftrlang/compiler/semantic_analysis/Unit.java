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
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.FieldManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;
import com.energyxxer.util.out.Console;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class Unit extends AbstractFileComponent implements Symbol, Context {
    private final CraftrFile declaringFile;
    private List<CraftrUtil.Modifier> modifiers;
    private final SymbolVisibility visibility;
    private final String name;
    private final UnitType type;

    private Unit superUnit = null;
    private List<Unit> features = null;

    private List<Token> rawUnitExtends = null;
    private List<List<Token>> rawUnitImplements = null;
    private List<List<Token>> rawUnitRequires = null;

    private FieldManager fieldManager;
    private MethodManager methodManager;

    private boolean unitActionsInitialized = false;

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

        Console.debug.println(modifiers);

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
                        System.out.println("flat = " + flat);
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

        //Parse body

        TokenPattern<?> componentList = pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST");
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

        Console.debug.println(this.toString());
    }

    public void initUnitActions() {
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

        this.features = new ArrayList<>();

        System.out.println("rawUnitImplements for " + this.name + " = " + rawUnitImplements);

        if(rawUnitImplements != null) {
            for(List<Token> path : rawUnitImplements) {
                System.out.println("path = " + path);
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

    public CraftrFile getDeclaringFile() {
        return declaringFile;
    }

    @Override
    public @Nullable Unit getUnit() {
        return this;
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
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

    @Override
    public String toString() {
        return name;
        /*return "" + modifiers + " " + type + " " + name + ""
                + ((rawUnitExtends != null) ? " extends " + rawUnitExtends: "")
                + ((rawUnitImplements != null) ? " implements " + rawUnitImplements: "")
                + ((rawUnitRequires != null) ? " requires " + rawUnitRequires: "");*/
    }
}