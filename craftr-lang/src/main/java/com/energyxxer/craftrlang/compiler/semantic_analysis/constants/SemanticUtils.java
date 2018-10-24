package com.energyxxer.craftrlang.compiler.semantic_analysis.constants;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.enxlex.pattern_matching.structures.TokenItem;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.enxlex.report.Notice;
import com.energyxxer.enxlex.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */
public final class SemanticUtils {
    public static List<CraftrLang.Modifier> getModifiers(List<TokenPattern<?>> modifierPatterns, SemanticAnalyzer analyzer) {
        ArrayList<CraftrLang.Modifier> modifiers = new ArrayList<>();
        for(TokenPattern<?> p : modifierPatterns) {
            String value = ((TokenItem) p).getContents().value;
            try {
                CraftrLang.Modifier modifier = CraftrLang.Modifier.valueOf(value.toUpperCase());
                if(!modifiers.contains(modifier)) modifiers.add(modifier);
                else analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate modifier '" + value + "'", p));
            } catch(IllegalArgumentException x) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unknown modifier '" + value + "'", p));
            }
        }
        return modifiers;
    }
}
