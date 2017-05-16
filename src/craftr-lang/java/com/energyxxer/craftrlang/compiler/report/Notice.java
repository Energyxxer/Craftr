package com.energyxxer.craftrlang.compiler.report;

import java.util.Arrays;

/**
 * Created by User on 5/15/2017.
 */
public class Notice extends Throwable {
    private NoticeType type;
    private String message;
    private String formattedPath;

    private String filePath;
    private int locationIndex;
    private int locationLength;
    private String pathDisplay;

    private String label;

    public Notice(NoticeType type, String message) {
        this(type, message, null);
    }

    public Notice(NoticeType type, String message, String formattedPath) {
        this.type = type;
        this.message = message;
        setFormattedPath(formattedPath);
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
        if(formattedPath != null) {
            String[] segments = formattedPath.split("\b");
            System.out.println(Arrays.toString(segments));
            this.filePath = segments[1];
            this.locationIndex = Integer.parseInt(segments[2]);
            this.locationLength = Integer.parseInt(segments[3]);
            this.pathDisplay = segments[4];

            this.label = this.filePath;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public int getLocationLength() {
        return locationLength;
    }

    public String getPathDisplay() {
        return pathDisplay;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return message + ((formattedPath != null) ? ("\n    at " + formattedPath) : "");
    }
}
