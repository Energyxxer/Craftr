package com.energyxxer.craftr.ui.editor.inspector;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;

/**
 * Created by User on 1/1/2017.
 */
public class InspectionItem {

    TokenPattern<?> pattern;
    InspectionType type;

    public InspectionItem(TokenPattern<?> pattern, InspectionType type) {
        this.pattern = pattern;
        this.type = type;
    }
}
