package com.energyxxer.craftr.compile.analysis.profiles;

import com.energyxxer.craftr.util.StringLocation;

/**
 * Created by User on 2/4/2017.
 */
public class AnalysisContextResponse {
    public final boolean success;
    public final String value;
    public final String tokenType;
    public final StringLocation endLocation;

    public AnalysisContextResponse(boolean success) {
        this(success,null, null, null);
    }

    public AnalysisContextResponse(boolean success, String value, StringLocation endLoc) {
        this(success, value, endLoc, null);
    }

    public AnalysisContextResponse(boolean success, String value, String tokenType) {
        this(success, value, new StringLocation(value.length()), tokenType);
    }

    public AnalysisContextResponse(boolean success, String value, StringLocation endLoc, String tokenType) {
        this.success = success;
        this.value = value;
        this.endLocation = endLoc;
        this.tokenType = tokenType;
    }
}
