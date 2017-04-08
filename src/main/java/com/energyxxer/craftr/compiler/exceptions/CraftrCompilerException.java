package com.energyxxer.craftr.compiler.exceptions;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;

/**
 * Created by User on 12/5/2016.
 */
public class CraftrCompilerException extends CraftrException {

    public CraftrCompilerException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }
    public CraftrCompilerException(String message) {
        super(message);
    }
}
