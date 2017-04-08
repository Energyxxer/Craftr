package com.energyxxer.craftr.compiler.lexical_analysis.token;

import com.energyxxer.craftr.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.matching.TokenPatternMatch;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/1/2017.
 */
public class TokenToolkit {

    public static ArrayList<TokenPattern<?>> search(List<Token> tokens, TokenPatternMatch m) {

        ArrayList<TokenPattern<?>> matches = new ArrayList<>();

        for(int i = 0; i < tokens.size(); i++) {
            TokenMatchResponse response = m.match(tokens.subList(i,tokens.size()));
            if(response.matched) {
                matches.add(response.pattern);
                i += response.length-1;
            }
        }

        return matches;
    }
}
