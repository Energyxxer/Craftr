package com.energyxxer.cbe.ui.theme;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.main.window.Window;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 12/13/2016.
 */
public class ThemeManager {

    private static HashMap<String, Theme> themes = new HashMap<>();
    private static String themeDirectory = "/resources/themes/";

    private static Theme nullTheme = new Theme("null");

    public static void loadAll() {
        themes.clear();

        URL url = Class.class.getResource(themeDirectory);

        try {
            File directory = new File(url.toURI());

            File[] themeFiles = directory.listFiles();
            ThemeReader tr = new ThemeReader();
            if(themeFiles == null) return;
            for(File file : themeFiles) {
                try {
                    Theme theme = tr.read(file);
                    themes.put(theme.getName(),theme);
                } catch(ThemeParserException e) {
                    System.out.println(e);
                }
            }
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Theme> getThemes() {
        return themes;
    }

    public static List<Theme> getThemesAsList() {
        return new ArrayList<>(themes.values());
    }

    public static Theme[] getThemesAsArray() {
        return getThemesAsList().toArray(new Theme[0]);
    }

    public static Theme getTheme(String name) {
        return (name.equals("null")) ? null : themes.get(name);
    }

    public static void setTheme(String name) {
        if(name != null && name.equals("null")) {
            Preferences.put("theme","null");
            Window.setTheme(nullTheme);
            return;
        }

        Theme theme = getTheme(name);
        if(theme != null) {
            Preferences.put("theme",theme.getName());
            Window.setTheme(theme);
        } else {
            Preferences.put("theme","null");
            Window.setTheme(new Theme("null"));
        }
    }
}
