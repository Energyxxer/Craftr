package com.energyxxer.craftrlang.compiler.exceptions;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;

/**
 * Created by User on 12/5/2016.
 */
public class CompilerException extends CraftrException {

    public CompilerException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }
    public CompilerException(String message) {
        super(message);
    }
}
