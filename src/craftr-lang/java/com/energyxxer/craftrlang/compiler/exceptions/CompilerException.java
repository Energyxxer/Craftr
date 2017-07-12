package com.energyxxer.craftrlang.compiler.exceptions;

import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;

/**
 * Created by User on 12/5/2016.
 */
public class CompilerException extends RuntimeException {

    private String formattedPath = null;
    private String errorCode = null;

    public CompilerException() {
    }

    public CompilerException(String message) {
        super(message);
    }

    public CompilerException(String message, String formattedPath) {
        super(message);
        this.formattedPath = formattedPath;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFormattedPath() {
        return formattedPath;
    }

    public void setFormattedPath(String formattedPath) {
        this.formattedPath = formattedPath;
    }

    public Notice getNotice() {
        return new Notice(NoticeType.ERROR, this.getMessage(), this.formattedPath);
    }
}
