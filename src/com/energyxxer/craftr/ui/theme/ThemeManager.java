package com.energyxxer.craftr.ui.theme;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.main.window.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 12/13/2016.
 */
public class ThemeManager {

    private static HashMap<String, Theme> themes = new HashMap<>();

    private static Theme nullTheme = new Theme("null");

    public static void loadAll() {
        themes.clear();

        ThemeReader tr = new ThemeReader();
        for(String file : Resources.indexes.get("Themes")) {
            try {
                Theme theme = tr.read(file);
                if(theme != null) themes.put(theme.getName(),theme);
            } catch(ThemeParserException e) {
                System.out.println(e.getMessage());
            }
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
