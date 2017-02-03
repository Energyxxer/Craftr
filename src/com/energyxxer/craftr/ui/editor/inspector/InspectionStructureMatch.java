package com.energyxxer.craftr.ui.editor.inspector;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.craftr.compile.analysis.token.TokenToolkit;
import com.energyxxer.craftr.compile.analysis.token.matching.TokenPatternMatch;
import com.energyxxer.craftr.compile.analysis.token.matching.TokenStructureMatch;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenList;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/1/2017.
 */
public class InspectionStructureMatch extends TokenStructureMatch {

    public InspectionType type;

    private TokenPatternMatch highlightPattern;
    private TokenPatternMatch excludePattern = null;

    public InspectionStructureMatch(String name, InspectionType type) {
        super(name);
        this.type = type;
    }

    public TokenPatternMatch getHighlight() {
        return highlightPattern;
    }

    public void setHighlight(TokenPatternMatch highlightPattern) {
        this.highlightPattern = highlightPattern;
    }

    public TokenPatternMatch getExclude() {
        return excludePattern;
    }

    public void setExclude(TokenPatternMatch excludePattern) {
        this.excludePattern = excludePattern;
    }

    @Override
    public TokenMatchResponse match(List<Token> tokens) {
        TokenMatchResponse match = super.match(tokens);
        if(match.matched && highlightPattern != null) {
            TokenList pattern = null;
            ArrayList<TokenPattern<?>> highlightMatches = TokenToolkit.search(tokens.subList(0, match.length), highlightPattern);
            for (TokenPattern<?> highlightMatch : highlightMatches) {
                if (excludePattern != null) {
                    if (!excludePattern.match(highlightMatch.flattenTokens()).matched) {
                        if (pattern == null) pattern = new TokenList();
                        pattern.add(highlightMatch);
                    }
                } else {
                    pattern = new TokenList();
                    pattern.add(highlightMatch);
                }
            }
            return new TokenMatchResponse(true, null, (pattern == null) ? 0 : match.length, pattern);

        } else return match;
    }
}
