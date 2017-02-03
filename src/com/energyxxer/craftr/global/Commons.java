package com.energyxxer.craftr.global;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import java.awt.*;

public class Commons {

    public static String DEFAULT_CARET_DISPLAY_TEXT = "-:-";

    public static String themeAssetsPath = "light_theme/";
    public static Color warningColor = new Color(255, 140, 0);
    public static Color errorColor = new Color(200,50,50);

    static {
        ThemeChangeListener.addThemeChangeListener(t -> {
            themeAssetsPath = t.getString("Assets.path","light_theme/");
            warningColor = t.getColor("Console.warning", new Color(255, 140, 0));
            errorColor = t.getColor("Console.error", new Color(200,50,50));
        }, true);
    }

    public static boolean isSpecialCharacter(char ch) {
        return "\b\r\n\t\f\u007F".contains("" + ch);
    }
}
