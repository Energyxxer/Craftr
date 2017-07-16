package com.energyxxer.craftrlang.compiler.semantic_analysis.constants;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */
public final class SemanticUtils {
    public static List<CraftrUtil.Modifier> getModifiers(List<TokenPattern<?>> modifierPatterns, SemanticAnalyzer analyzer) {
        ArrayList<CraftrUtil.Modifier> modifiers = new ArrayList<>();
        for(TokenPattern<?> p : modifierPatterns) {
            String value = ((TokenItem) p).getContents().value;
            try {
                CraftrUtil.Modifier modifier = CraftrUtil.Modifier.valueOf(value.toUpperCase());
                if(!modifiers.contains(modifier)) modifiers.add(modifier);
                else analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate modifier '" + value + "'", p.getFormattedPath()));
            } catch(IllegalArgumentException x) {
                analyzer.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unknown modifier '" + value + "'", p.getFormattedPath()));
            }
        }
        return modifiers;
    }
}
