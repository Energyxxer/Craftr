package com.energyxxer.craftr.ui.editor.inspector;

/**
 * Created by User on 1/1/2017.
 */
public enum InspectionType {

    SUGGESTION("Inspector.suggestion"), WARNING("Inspector.warning", true), ERROR("Inspector.error", true);

    public String colorKey;
    public boolean line;

    InspectionType(String colorKey) {
        this.colorKey = colorKey;
        line = false;
    }

    InspectionType(String colorKey, boolean line) {
        this.colorKey = colorKey;
        this.line = line;
    }
}
