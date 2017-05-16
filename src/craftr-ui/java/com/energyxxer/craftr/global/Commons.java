package com.energyxxer.craftr.global;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.craftrlang.projects.ProjectManager;
import com.energyxxer.util.ImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Commons {

    public static String DEFAULT_CARET_DISPLAY_TEXT = "-:-";

    public static String themeAssetsPath = "light_theme/";

    static {
        ThemeChangeListener.addThemeChangeListener(t -> {
            themeAssetsPath = t.getString("Assets.path","default:light_theme/");
        }, true);
    }

    public static boolean isSpecialCharacter(char ch) {
        return "\b\r\n\t\f\u007F\u001B".contains("" + ch);
    }

    public static void showInExplorer(String path) {
        try {
            Runtime.getRuntime().exec("Explorer.exe /select," + path);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private static String getIconPath(String name) {
        return "/assets/icons/" + themeAssetsPath + name + ".png";
    }

    public static BufferedImage getIcon(String name) {
        return ImageManager.load(getIconPath(name));
    }

    public static Project getSelectedProject() {
        Project selected = null;

        Tab selectedTab = TabManager.getSelectedTab();

        List<String> selectedFiles = CraftrWindow.projectExplorer.getSelectedFiles();

        if(selectedFiles.size() > 0) {
            selected = ProjectManager.getAssociatedProject(new File(selectedFiles.get(0)));
        } else if(selectedTab != null) {
            selected = selectedTab.getLinkedProject();
        }
        return selected;
    }
}
