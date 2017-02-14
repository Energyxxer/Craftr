package com.energyxxer.craftr.global;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.ImageManager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    public static void showInExplorer(String path) {
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + path);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    public static String getIconPath(String name) {
        return "/assets/icons/" + themeAssetsPath + name + ".png";
    }

    public static BufferedImage getIcon(String name) {
        return ImageManager.load(getIconPath(name));
    }
}
