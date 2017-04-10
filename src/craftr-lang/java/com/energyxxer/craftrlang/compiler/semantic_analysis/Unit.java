package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.exceptions.ParserException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.Access;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.util.out.Console;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class Unit extends AbstractFileComponent implements Symbol {
    private CraftrFile declaringFile;
    public List<CraftrUtil.Modifier> modifiers;
    public String name;
    public String type;

    public String unitExtends = null;
    public List<String> unitImplements = null;
    public List<String> unitRequires = null;

    public List<Field> fields;

    public Unit(CraftrFile file, TokenPattern<?> pattern) throws CraftrException {
        super(pattern);
        this.declaringFile = file;

        //Parse header

        TokenPattern<?> header = pattern.find("UNIT_DECLARATION");

        this.name = ((TokenItem) header.find("UNIT_NAME")).getContents().value;
        this.type = ((TokenItem) header.find("UNIT_TYPE")).getContents().value;

        this.modifiers = SemanticUtils.getModifiers(header.deepSearchByName("UNIT_MODIFIER"));

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

        //Parse body

        this.fields = new ArrayList<>();

        TokenPattern<?> componentList = pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST");
        if(componentList != null) {
            for (TokenPattern<?> p : componentList.searchByName("UNIT_COMPONENT")) {
                TokenStructure component = (TokenStructure) p.getContents();
                if (component.getName().equals("FIELD")) {
                    fields.addAll(Field.parseDeclaration(this, component));
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
    public Access getAccess() {
        return modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? Access.PUBLIC : Access.PACKAGE;
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
