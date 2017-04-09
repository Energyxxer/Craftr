package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.exceptions.CraftrException;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.compiler.semantic_analysis.constants.Access;
import com.energyxxer.craftr.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftr.compiler.semantic_analysis.context.CraftrSymbol;
import com.energyxxer.craftr.compiler.semantic_analysis.data_types.CraftrDataType;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.CraftrUtil;
import com.energyxxer.craftr.global.CraftrUtil.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2/26/2017.
 */
public class CraftrField extends AbstractCraftrComponent implements CraftrSymbol {
    private CraftrUnit declaringUnit;

    private List<Modifier> modifiers;
    private CraftrDataType type;
    private String name;

    public CraftrField(CraftrUnit declaringUnit, TokenPattern<?> pattern, List<Modifier> modifiers, CraftrDataType type) {
        super(pattern);
        this.declaringUnit = declaringUnit;
        this.modifiers = new ArrayList<>();
        this.modifiers.addAll(modifiers);
        this.type = null;

        this.name = ((TokenItem) pattern.find("FIELD_NAME")).getContents().value;

        Console.debug.println("at " + declaringUnit + "#" + name);
    }

    public static List<CraftrField> parseDeclaration(CraftrUnit declaringUnit, TokenPattern<?> pattern) throws CraftrException {
        ArrayList<CraftrField> fields = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()));

        Console.debug.println("[Field] Modifiers: " + modifiers);

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.FIELD_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("FIELD_DECLARATION")) continue;
            fields.add(new CraftrField(declaringUnit, p, modifiers, null));
        }

        return fields;
    }

    @Override
    public String getName() {
        return name;
    }

    public Access getAccess() {
        return modifiers.contains(Modifier.PUBLIC) ? Access.PUBLIC :
               modifiers.contains(Modifier.PROTECTED) ? Access.PROTECTED :
               modifiers.contains(Modifier.PRIVATE) ? Access.PRIVATE :
               Access.PACKAGE;
    }
}
