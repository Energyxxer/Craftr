package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.compiler.semantic_analysis.data_types.CraftrDataType;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.CraftrUtil.Modifier;

import java.util.List;

/**
 * Created by User on 2/26/2017.
 */
public class CraftrField extends AbstractCraftrComponent {
    private CraftrUnit declaringUnit;

    private List<Modifier> modifiers;
    private CraftrDataType type;

    public CraftrField(CraftrUnit declaringUnit, TokenPattern<?> pattern, List<Modifier> modifiers, CraftrDataType type) {
        super(pattern);
        this.declaringUnit = declaringUnit;

        Console.debug.println(pattern);
    }

    public static List<CraftrField> parseDeclaration(CraftrUnit declaringUnit, TokenPattern<?> pattern) {
        return null;
    }
}
