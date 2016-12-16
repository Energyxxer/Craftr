package com.energyxxer.cbe.compile.parsing.exceptions;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.util.ColorUtil;

/**
 * Created by User on 12/4/2016.
 */
public class CBEParserException extends Throwable {

    public CBEParserException(String message, Token faultyToken) {
        super("<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">" + message + "\n\tat </span>" + faultyToken.getFormattedPath());
    }

    public CBEParserException(String message, TokenPattern<?> pattern) {
        super("<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">" + message + "\n\tat </span>" + pattern.getFormattedPath());
    }
}
