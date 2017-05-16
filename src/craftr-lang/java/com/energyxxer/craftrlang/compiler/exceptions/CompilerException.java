package com.energyxxer.craftrlang.compiler.exceptions;

/**
 * Created by User on 12/5/2016.
 */
public class CompilerException extends RuntimeException {

    private String errorCode = null;

    public CompilerException() {
    }

    public CompilerException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
