package com.energyxxer.craftrlang.compiler.exceptions;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;

/**
 * Created by User on 12/4/2016.
 */
public class ParserException extends CraftrException {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }

    public ParserException(String message, TokenPattern<?> pattern) {
        super(message + "\n\tat " + pattern.getFormattedPath());
    }
}
