package com.energyxxer.craftr.global;

import com.energyxxer.craftr.compile.analysis.presets.CraftrAnalysisProfile;
import com.energyxxer.craftr.compile.analysis.presets.JSONAnalysisProfile;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisProfile;
import com.energyxxer.craftr.ui.components.factory.Factory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 2/9/2017.
 */
public enum Lang {
    CRAFTR(CraftrAnalysisProfile::new, "craftr"), JSON(JSONAnalysisProfile::new, "json", "mcmeta");

    Factory<AnalysisProfile> factory;
    List<String> extensions;

    Lang(Factory<AnalysisProfile> factory, String... extensions) {
        this.factory = factory;
        this.extensions = Arrays.asList(extensions);
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public AnalysisProfile createProfile() {
        return factory.createInstance();
    }

    public static Lang getLangForFile(String path) {
        for(Lang lang : Lang.values()) {
            for(String extension : lang.extensions) {
                if(path.endsWith("." + extension)) {
                    return lang;
                }
            }
        }
        return null;
    }
}