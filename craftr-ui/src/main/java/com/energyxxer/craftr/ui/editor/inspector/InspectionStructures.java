package com.energyxxer.craftr.ui.editor.inspector;

import com.energyxxer.craftrlang.compiler.parsing.CraftrProductions;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenGroupMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenItemMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenListMatch;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2017.
 */
public class InspectionStructures {
    public final static InspectionStructureMatch UNREACHABLE_CODE;
    public final static InspectionStructureMatch EMPTY_CODE_BLOCK;

    private static ArrayList<InspectionStructureMatch> all = new ArrayList<>();

    static {
        UNREACHABLE_CODE = new InspectionStructureMatch("Unreachable code", InspectionType.ERROR);

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(CraftrProductions.RETURN_EXPRESSION);
            g.append(new TokenListMatch(CraftrProductions.STATEMENT));

            UNREACHABLE_CODE.add(g);
            UNREACHABLE_CODE.setHighlight(CraftrProductions.STATEMENT);
            UNREACHABLE_CODE.setExclude(CraftrProductions.RETURN_EXPRESSION);
        }

        all.add(UNREACHABLE_CODE);

        EMPTY_CODE_BLOCK = new InspectionStructureMatch("Empty code block", InspectionType.SUGGESTION);

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(TokenType.BRACE,"{"));
            g.append(new TokenItemMatch(TokenType.BRACE,"}"));

            EMPTY_CODE_BLOCK.add(g);
        }

        all.add(EMPTY_CODE_BLOCK);
    }

    public static ArrayList<InspectionStructureMatch> getAll() {
        return all;
    }
}
