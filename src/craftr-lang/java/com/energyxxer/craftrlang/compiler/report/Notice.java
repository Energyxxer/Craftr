package com.energyxxer.craftrlang.compiler.report;

/**
 * Created by User on 5/15/2017.
 */
public class Notice extends Throwable {
    private NoticeType type;
    private String message;
    private String formattedPath;

    public Notice(NoticeType type, String message) {
        this(type, message, null);
    }

    public Notice(NoticeType type, String message, String formattedPath) {
        this.type = type;
        this.message = message;
        this.formattedPath = formattedPath;
    }

    public NoticeType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedPath() {
        return formattedPath;
    }

    public void setType(NoticeType type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFormattedPath(String formattedPath) {
        this.formattedPath = formattedPath;
    }

    @Override
    public String toString() {
        return message + ((formattedPath != null) ? ("\n    at " + formattedPath) : "");
    }
}
