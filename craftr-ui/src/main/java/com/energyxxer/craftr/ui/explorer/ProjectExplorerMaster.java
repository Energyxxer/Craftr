package com.energyxxer.craftr.ui.explorer;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.ui.explorer.base.ExplorerMaster;
import com.energyxxer.craftr.ui.explorer.base.elements.ExplorerSeparator;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.craftrlang.projects.ProjectManager;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by User on 5/16/2017.
 */
public class ProjectExplorerMaster extends ExplorerMaster {
    private final File root;

    private ThemeListenerManager tlm = new ThemeListenerManager();

    public ProjectExplorerMaster(File root) {
        this.root = root;

        tlm.addThemeChangeListener(t -> {
            colors.put("background",t.getColor(Color.WHITE, "Explorer.background"));
            colors.put("item.background",t.getColor(new Color(0,0,0,0), "Explorer.item.background"));
            colors.put("item.foreground",t.getColor(Color.BLACK, "Explorer.item.foreground","General.foreground"));
            colors.put("item.selected.background",t.getColor(Color.BLUE, "Explorer.item.selected.background","Explorer.item.background"));
            colors.put("item.selected.foreground",t.getColor(Color.BLACK, "Explorer.item.selected.foreground","Explorer.item.hover.foreground","Explorer.item.foreground","General.foreground"));
            colors.put("item.rollover.background",t.getColor(new Color(0,0,0,0), "Explorer.item.hover.background","Explorer.item.background"));
            colors.put("item.rollover.foreground",t.getColor(Color.BLACK, "Explorer.item.hover.foreground","Explorer.item.foreground","General.foreground"));

            rowHeight = Math.max(t.getInteger(20,"Explorer.item.height"), 1);
            indentPerLevel = Math.max(t.getInteger(20,"Explorer.item.indent"), 0);
            initialIndent = Math.max(t.getInteger(0,"Explorer.item.initialIndent"), 0);

            selectionStyle = t.getString("Explorer.item.selectionStyle","default:FULL");
            selectionLineThickness = Math.max(t.getInteger(2,"Explorer.item.selectionLineThickness"), 0);

            assets.put("expand", Commons.getIcon("expand").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            assets.put("collapse",Commons.getIcon("collapse").getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        });

        refresh();
    }

    @Override
    public void refresh() {
        ProjectManager.setWorkspaceDir(Preferences.get("workspace_dir"));
        ProjectManager.loadWorkspace();

        ArrayList<String> copy = new ArrayList<>();
        copy.addAll(this.getExpandedElements());
        refresh(copy);
    }

    private void refresh(ArrayList<String> toOpen) {
        children.clear();
        flatList.clear();
        this.getExpandedElements().clear();

        File[] subfiles = root.listFiles();
        if(subfiles == null) return;

        ArrayList<File> subfiles1 = new ArrayList<>();

        for(File f : subfiles) {
            if(f.isDirectory()) {
                this.children.add(new ProjectExplorerItem(this, f, toOpen));
            } else {
                subfiles1.add(f);
            }
        }
        for(File f : subfiles1) {
            this.children.add(new ProjectExplorerItem(this, f, toOpen));
        }

        this.children.add(new ExplorerSeparator(this));

        File[] resourceFiles = new File(System.getProperty("user.home") + File.separator + "Craftr" + File.separator + "resources").listFiles();
        if(resourceFiles != null) {
            for(File f : resourceFiles) {
                this.children.add(new ProjectExplorerItem(this, f, toOpen));
            }
        }

        repaint();
    }
}
