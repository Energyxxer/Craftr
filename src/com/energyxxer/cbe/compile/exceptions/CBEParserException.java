package com.energyxxer.cbe.compile.exceptions;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;

/**
 * Created by User on 12/4/2016.
 */
public class CBEParserException extends CBEException {

    public CBEParserException(String message) {
        super(message);
    }

    public CBEParserException(String message, Token faultyToken) {
        super(message + "\n\tat " + faultyToken.getFormattedPath());
    }

    public CBEParserException(String message, TokenPattern<?> pattern) {
        super(message + "\n\tat " + pattern.getFormattedPath());
    }
}
