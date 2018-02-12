package com.energyxxer.craftr.global;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.craftrlang.projects.ProjectManager;
import com.energyxxer.util.ImageManager;
import com.energyxxer.util.out.Console;

import java.awt.*;
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
            if(System.getProperty("os.name").startsWith("Windows")) {
                Runtime.getRuntime().exec("Explorer.exe /select," + path);
            } else if(Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(path).getParentFile());
            } else {
                Console.err.println("Couldn't open file '" + path + "': Desktop is not supported");
            }
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

    public static void updateActiveProject() {
        if(CraftrWindow.toolbar != null && CraftrWindow.projectExplorer != null)
            CraftrWindow.toolbar.setActiveProject(getActiveProject());
    }

    public static Project getActiveProject() {
        Project selected = null;

        Tab selectedTab = TabManager.getSelectedTab();

        List<String> selectedFiles = CraftrWindow.projectExplorer.getSelectedFiles();

        if(selectedTab != null && selectedTab.getLinkedProject() != null) {
            selected = selectedTab.getLinkedProject();
        } else if(selectedFiles.size() > 0) {
            selected = ProjectManager.getAssociatedProject(new File(selectedFiles.get(0)));
        }
        return selected;
    }
}
