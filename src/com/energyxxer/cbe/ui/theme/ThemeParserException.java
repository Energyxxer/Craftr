package com.energyxxer.cbe.ui.theme;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.util.ColorUtil;

/**
 * Created by User on 12/13/2016.
 */
public class ThemeParserException extends Throwable {

    public ThemeParserException(String message) {
        super("<span style=\"color:" + ColorUtil.toCSS(Commons.warningColor) + ";\">" + message + "</span>");
    }
    public ThemeParserException(String message, int lineNumber, String line) {
        super("<span style=\"color:" + ColorUtil.toCSS(Commons.warningColor) + ";\">" + message + "\n\tat line " + lineNumber + ":\n\t" + line + "</span>");
    }
}
