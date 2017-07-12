package com.energyxxer.craftrlang.compiler.semantic_analysis.constants;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.exceptions.CompilerException;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */
public final class SemanticUtils {
    public static List<CraftrUtil.Modifier> getModifiers(List<TokenPattern<?>> modifierPatterns) throws CompilerException {
        ArrayList<CraftrUtil.Modifier> modifiers = new ArrayList<>();
        for(TokenPattern<?> p : modifierPatterns) {
            String value = ((TokenItem) p).getContents().value;
            try {
                CraftrUtil.Modifier modifier = CraftrUtil.Modifier.valueOf(value.toUpperCase());
                if(!modifiers.contains(modifier)) modifiers.add(modifier);
                else {
                    throw new CompilerException("Duplicate modifier '" + value + '\'', p.getFormattedPath());
                }
            } catch(IllegalArgumentException x) {
                throw new CompilerException("Unknown modifier '" + value + '\'', p.getFormattedPath());
            }
        }
        return modifiers;
    }
}
