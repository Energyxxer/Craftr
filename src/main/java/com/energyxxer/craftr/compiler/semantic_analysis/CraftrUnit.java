package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.exceptions.CraftrException;
import com.energyxxer.craftr.compiler.exceptions.CraftrParserException;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftr.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.CraftrUtil.Modifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/25/2017.
 */
public class CraftrUnit extends AbstractCraftrComponent {
    private CraftrFile declaringFile;
    public List<Modifier> modifiers;
    public String name;
    public String type;

    public String unitExtends = null;
    public List<String> unitImplements = null;
    public List<String> unitRequires = null;

    public List<CraftrField> fields;

    public CraftrUnit(CraftrFile file, TokenPattern<?> pattern) throws CraftrException {
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
                    if(unitExtends != null) throw new CraftrParserException("Duplicate unit action 'extends'", p);

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    if(references.size() > 1) throw new CraftrParserException("Unit cannot extend multiple units", p);

                    unitExtends = references.get(0).flatten(false);
                    break;
                }
                case "implements": {
                    if(unitImplements != null) throw new CraftrParserException("Duplicate unit action 'implements'", p);
                    unitImplements = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        String str = reference.flatten(false);
                        if(!unitImplements.contains(str)) unitImplements.add(str);
                        else throw new CraftrParserException("Duplicate unit '" + str + "'");
                    }
                    break;
                }
                case "requires": {
                    if(unitRequires != null) throw new CraftrParserException("Duplicate unit action 'requires'", p);
                    unitRequires = new ArrayList<>();

                    List<TokenPattern<?>> references = p.deepSearchByName("UNIT_ACTION_REFERENCE");
                    for(TokenPattern<?> reference : references) {
                        String str = reference.flatten(false);
                        if(!unitRequires.contains(str)) unitRequires.add(str);
                        else throw new CraftrParserException("Duplicate unit '" + str + "'");
                    }
                    break;
                }
                default: {
                    Console.warn.println("Unit action \"" + actionType + "\"");
                }
            }
        }

        //Parse body

        this.fields = new ArrayList<>();

        TokenPattern<?> componentList = pattern.find("UNIT_BODY.UNIT_COMPONENT_LIST");
        if(componentList != null) {
            for (TokenPattern<?> p : componentList.searchByName("UNIT_COMPONENT")) {
                TokenStructure component = (TokenStructure) p.getContents();
                if (component.name.equals("FIELD")) {
                    fields.addAll(CraftrField.parseDeclaration(this, component));
                }
            }
        }

        Console.debug.println(this.toString());
    }

    @Override
    public String toString() {
        return "" + modifiers + " " + type + " " + name + " "
                + ((unitExtends != null) ? "extends " + unitExtends + " " : "")
                + ((unitImplements != null) ? "implements " + unitImplements + " " : "")
                + ((unitRequires != null) ? "requires " + unitRequires + " " : "");
    }
}
