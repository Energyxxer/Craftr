package com.energyxxer.cbe.compile.exceptions;

import com.energyxxer.cbe.compile.analysis.token.Token;

/**
 * Created by User on 12/5/2016.
 */
public class CBECompilerException extends CBEException {

    public CBECompilerException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }
    public CBECompilerException(String message) {
        super(message);
    }
}
