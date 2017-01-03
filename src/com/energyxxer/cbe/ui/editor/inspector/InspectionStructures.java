package com.energyxxer.cbe.ui.editor.inspector;

import com.energyxxer.cbe.compile.analysis.LangStructures;
import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenGroupMatch;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenItemMatch;
import com.energyxxer.cbe.compile.analysis.token.matching.TokenListMatch;

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
            g.append(LangStructures.RETURN_STATEMENT);
            g.append(new TokenListMatch(LangStructures.STATEMENT));

            UNREACHABLE_CODE.add(g);
            UNREACHABLE_CODE.setHighlight(LangStructures.STATEMENT);
            UNREACHABLE_CODE.setExclude(LangStructures.RETURN_STATEMENT);
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
