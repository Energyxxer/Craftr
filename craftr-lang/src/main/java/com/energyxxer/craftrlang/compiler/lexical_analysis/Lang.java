package com.energyxxer.craftrlang.compiler.lexical_analysis;

import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.CraftrScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.JSONScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.MCFunctionScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.PropertiesScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftrlang.compiler.parsing.CraftrProductions;
import com.energyxxer.craftrlang.compiler.parsing.MCFunctionProductions;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenPatternMatch;
import com.energyxxer.util.Factory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 2/9/2017.
 */
public enum Lang {
    CRAFTR(CraftrScannerProfile::new, CraftrProductions.FILE, "craftr"), JSON(JSONScannerProfile::new, "json", "mcmeta"), PROPERTIES(PropertiesScannerProfile::new, "properties", "lang", "project"), MCFUNCTION(MCFunctionScannerProfile::new, MCFunctionProductions.FILE, "mcfunction");

    Factory<ScannerProfile> factory;
    TokenPatternMatch parserProduction;
    List<String> extensions;

    Lang(Factory<ScannerProfile> factory, String... extensions) {
        this(factory, null, extensions);
    }

    Lang(Factory<ScannerProfile> factory, TokenPatternMatch parserProduction, String... extensions) {
        this.factory = factory;
        this.parserProduction = parserProduction;
        this.extensions = Arrays.asList(extensions);
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public ScannerProfile createProfile() {
        return factory.createInstance();
    }

    public TokenPatternMatch getParserProduction() {
        return parserProduction;
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