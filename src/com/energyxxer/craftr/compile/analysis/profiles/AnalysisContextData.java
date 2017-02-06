package com.energyxxer.craftr.compile.analysis.profiles;

import java.util.HashMap;

/**
 * Created by User on 2/4/2017.
 */
public class AnalysisContextData {

    private HashMap<AnalysisContext, Object> data = new HashMap<>();

    public AnalysisContextData() {
    }

    public Object get(AnalysisContext parent) {
        return this.data.get(parent);
    }

    public void put(AnalysisContext parent, Object data) {
        this.data.put(parent, data);
    }
}
