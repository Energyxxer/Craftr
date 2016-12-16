package com.energyxxer.cbe.compile.parsing.exceptions;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.util.ColorUtil;

/**
 * Created by User on 12/5/2016.
 */
public class CBECompilerException extends Throwable {

    public CBECompilerException(String message, Token faultyToken) {
        super("<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">" + message + "\n\tat </span>" + faultyToken.getFormattedPath());
    }
}
