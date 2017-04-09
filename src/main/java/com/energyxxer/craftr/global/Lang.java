package com.energyxxer.craftr.global;

import com.energyxxer.craftr.compiler.lexical_analysis.presets.CraftrScannerProfile;
import com.energyxxer.craftr.compiler.lexical_analysis.presets.JSONScannerProfile;
import com.energyxxer.craftr.compiler.lexical_analysis.presets.PropertiesScannerProfile;
import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftr.ui.components.factory.Factory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 2/9/2017.
 */
public enum Lang {
    CRAFTR(CraftrScannerProfile::new, "craftr"), JSON(JSONScannerProfile::new, "json", "mcmeta"), PROPERTIES(PropertiesScannerProfile::new, "properties", "lang", "project");

    Factory<ScannerProfile> factory;
    List<String> extensions;

    Lang(Factory<ScannerProfile> factory, String... extensions) {
        this.factory = factory;
        this.extensions = Arrays.asList(extensions);
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public ScannerProfile createProfile() {
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