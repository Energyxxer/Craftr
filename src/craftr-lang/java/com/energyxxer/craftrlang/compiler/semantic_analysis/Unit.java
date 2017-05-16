package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.exceptions.ParserException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Field;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;
import com.energyxxer.util.out.Console;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class Unit extends AbstractFileComponent implements Symbol {
    private final CraftrFile declaringFile;
    public List<CraftrUtil.Modifier> modifiers;
    public final SymbolVisibility visibility;
    public String name;
    public String type;

    public String unitExtends = null;
    public List<String> unitImplements = null;
    public List<String> unitRequires = null;

    private List<Field> fields;
    private List<Method> methods;

    private SymbolTable staticSymbolTable;
    private SymbolTable instanceSymbolTable;

    private Context context;

    public Unit(CraftrFile file, TokenPattern<?> pattern) throws CraftrException {
        super(pattern);
        this.declaringFile = file;
        this.context = new Context(this);

        //Parse header

        TokenPattern<?> header = pattern.find("UNIT_DECLARATION");

        this.name = ((TokenItem) header.find("UNIT_NAME")).getContents().value;
        this.type = ((TokenItem) header.find("UNIT_TYPE")).getContents().value;

        if(this.type.equals("entity") && !Character.isLowerCase(name.charAt(0))) declaringFile.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "Entity name '" + this.name + "' does not follow Craftr naming conventions", header.find("UNIT_NAME").getFormattedPath()));

        this.modifiers = SemanticUtils.getModifiers(header.deepSearchByName("UNIT_MODIFIER"));

        Console.debug.println(modifiers);

        List<TokenPattern<?>> actionPatterns = header.deepSearchByName("UNIT_ACTION");
        for(TokenPattern<?> p : actionPatterns) {
            String actionType = ((TokenItem) p.find("UNIT_ACTION_TYPE")).getContents().value;
            switch(actionType) {
                case "extends": {
                    if(unitExtends != null) throw new ParserException("Duplicate unit action 'extends'", p);

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    if(references.size() > 1) throw new ParserException("Unit cannot extend multiple units", p);

                    unitExtends = references.get(0).flatten(false);
                    break;
                }
                case "implements": {
                    if(unitImplements != null) throw new ParserException("Duplicate unit action 'implements'", p);
                    unitImplements = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        String str = reference.flatten(false);
                        if(!unitImplements.contains(str)) unitImplements.add(str);
                        else throw new ParserException("Duplicate unit '" + str + "'");
                    }
                    break;
                }
                case "requires": {
                    if(unitRequires != null) throw new ParserException("Duplicate unit action 'requires'", p);
                    unitRequires = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        String str = reference.flatten(false);
                        if(!unitRequires.contains(str)) unitRequires.add(str);
                        else throw new ParserException("Duplicate unit '" + str + "'");
                    }
                    break;
                }
                default: {
                    Console.debug.println("Unit action \"" + actionType + "\"");
                }
            }
        }

        this.visibility = modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL : SymbolVisibility.PACKAGE;

        this.staticSymbolTable = new SymbolTable(this.visibility, file.getPackage().getSubSymbolTable());
        file.getPackage().getSubSymbolTable().put(this);

        this.instanceSymbolTable = new SymbolTable(file.getAnalyzer().getCompiler());

        //Parse body

        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();

        TokenPattern<?> componentList = pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST");
        if(componentList != null) {
            for (TokenPattern<?> p : componentList.searchByName("UNIT_COMPONENT")) {
                TokenStructure component = (TokenStructure) p.getContents();
                if (component.getName().equals("FIELD")) {
                    fields.addAll(Field.parseDeclaration(this, component));
                } else if(component.getName().equals("METHOD")) {
                    methods.add(new Method(this, component));
                }
            }
        }


        Console.debug.println(this.toString());
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
    public @Nullable Package getPackage() {
        return declaringFile.getPackage();
    }

    public SymbolTable getSubSymbolTable() {
        return staticSymbolTable;
    }

    public CraftrFile getDeclaringFile() {
        return declaringFile;
    }

    public Context getContext() {
        return context;
    }

    public SymbolTable getStaticSymbolTable() {
        return staticSymbolTable;
    }

    public SymbolTable getInstanceSymbolTable() {
        return instanceSymbolTable;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return name;
        /*return "" + modifiers + " " + type + " " + name + ""
                + ((unitExtends != null) ? " extends " + unitExtends: "")
                + ((unitImplements != null) ? " implements " + unitImplements: "")
                + ((unitRequires != null) ? " requires " + unitRequires: "");*/
    }
}
