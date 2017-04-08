package com.energyxxer.craftr.compiler.exceptions;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;

/**
 * Created by User on 1/22/2017.
 */
public class CraftrException extends Throwable {

    public CraftrException(String message) {
        super(message);
    }

    public CraftrException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }

    public CraftrException(String message, TokenPattern<?> pattern) {
        super(message + "\n\tat " + pattern.getFormattedPath());
    }
}
