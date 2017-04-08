package com.energyxxer.craftr.compiler.exceptions;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;

/**
 * Created by User on 12/4/2016.
 */
public class CraftrParserException extends CraftrException {

    public CraftrParserException(String message) {
        super(message);
    }

    public CraftrParserException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }

    public CraftrParserException(String message, TokenPattern<?> pattern) {
        super(message + "\n\tat " + pattern.getFormattedPath());
    }
}
