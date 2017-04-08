package com.energyxxer.craftr.ui.theme;

import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.Lang;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.main.window.CraftrWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 12/13/2016.
 */
public class ThemeManager {

    private static HashMap<String, Theme> gui_themes = new HashMap<>();
    private static HashMap<String, Theme> syntax_themes = new HashMap<>();

    private static Theme nullTheme = new Theme("null");

    public static Theme currentGUITheme = new Theme("null");

    public static void loadAll() {
        gui_themes.clear();

        ThemeReader tr = new ThemeReader();
        for(String file : Resources.indexes.get("GUI Themes")) {
            try {
                Theme theme = tr.read(Theme.ThemeType.GUI_THEME, file);
                if(theme != null) gui_themes.put(theme.getName(),theme);
            } catch(ThemeParserException e) {
                Console.warn.println(e.getMessage());
            }
        }

        for(String file : Resources.indexes.get("Syntax Themes")) {
            try {
                Theme theme = tr.read(Theme.ThemeType.SYNTAX_THEME, file);
                if(theme != null) syntax_themes.put(theme.getName(),theme);
            } catch(ThemeParserException e) {
                Console.warn.println(e.getMessage());
            }
        }

        //Read theme directory

        String themeDirPath = System.getProperty("user.home") + File.separator + "Craftr" + File.separator + "resources" + File.separator + "themes" + File.separator;

        File themeDir = new File(themeDirPath);
        if(themeDir.exists()) {
            File guiThemeDirectory = new File(themeDirPath + "gui");
            if(guiThemeDirectory.exists()) {
                File[] files = guiThemeDirectory.listFiles();
                if(files != null) {
                    for(File file : files) {
                        try {
                            Theme theme = tr.read(Theme.ThemeType.GUI_THEME, file);
                            if(theme != null) gui_themes.put(theme.getName(),theme);
                        } catch(ThemeParserException e) {
                            Console.warn.println(e.getMessage());
                        }
                    }
                }

            } else guiThemeDirectory.mkdir();

            File syntaxThemeDirectory = new File(themeDirPath + "syntax");
            if(syntaxThemeDirectory.exists()) {
                File[] files = syntaxThemeDirectory.listFiles();
                if(files != null) {
                    for(File file : files) {
                        try {
                            Theme theme = tr.read(Theme.ThemeType.SYNTAX_THEME, file);
                            if(theme != null) syntax_themes.put(theme.getName(),theme);
                        } catch(ThemeParserException e) {
                            Console.warn.println(e.getMessage());
                        }
                    }
                }

            } else syntaxThemeDirectory.mkdir();
        } else themeDir.mkdirs();

        setGUITheme(Preferences.get("theme"));
    }

    public static HashMap<String, Theme> getGUIThemes() {
        return gui_themes;
    }

    public static List<Theme> getGUIThemesAsList() {
        return new ArrayList<>(gui_themes.values());
    }

    public static Theme[] getGUIThemesAsArray() {
        return getGUIThemesAsList().toArray(new Theme[0]);
    }

    public static Theme getGUITheme(String name) {
        return (name.equals("null")) ? null : gui_themes.get(name);
    }

    public static Theme getSyntaxTheme(String name) {
        return (name == null || name.equals("null")) ? null : syntax_themes.get(name);
    }

    public static void setGUITheme(String name) {
        if(name != null && name.equals("null")) {
            Preferences.put("theme","null");
            CraftrWindow.setTheme(nullTheme);
            currentGUITheme = nullTheme;
            return;
        }

        Theme theme = getGUITheme(name);
        if(theme == null) theme = nullTheme;

        Preferences.put("theme",theme.getName());
        CraftrWindow.setTheme(theme);
        currentGUITheme = theme;

    }

    public static Theme getSyntaxForGUITheme(Lang lang, Theme guiTheme) {
        String s = guiTheme.getString("Syntax." + lang.toString().toLowerCase());
        Console.debug.println(s);
        return getSyntaxTheme(s);
    }
}
