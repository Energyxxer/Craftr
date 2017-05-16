package com.energyxxer.craftr.ui.explorer.base.elements;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.*;
import com.energyxxer.craftr.ui.explorer.base.ExplorerFlag;
import com.energyxxer.craftr.ui.explorer.base.ExplorerMaster;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.craftr.ui.common.MenuItems;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenu;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.craftr.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftrlang.projects.ProjectManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/7/2017.
 */
public class ExplorerItem extends ExplorerElement {
    private final ExplorerMaster master;
    private ExplorerItem parent = null;

    private String path = null;
    private boolean isDirectory = false;
    private String filename = null;
    private int indentation = 0;

    private boolean expanded = false;

    private Image icon = null;

    private int x = 0;

    public ExplorerItem(ExplorerMaster master, File file, ArrayList<String> toOpen) {
        this.path = file.getPath();
        this.master = master;

        this.isDirectory = file.isDirectory();

        this.filename = file.getName();
        if(!this.isDirectory) {
            if(filename.endsWith(".craftr") || filename.endsWith(".png")) filename = filename.substring(0, filename.lastIndexOf('.'));
        }

        this.addThemeListener();

        if(toOpen.contains(this.path)) {
            expand(toOpen);
        }
    }

    private ExplorerItem(@NotNull ExplorerItem parent, File file, ArrayList<String> toOpen) {
        this.parent = parent;
        this.master = parent.master;

        File[] subfiles;
        StringBuilder filenameBuilder = new StringBuilder(file.getName());
        while(this.master.getFlag(ExplorerFlag.FLATTEN_EMPTY_PACKAGES) && canFlatten(file) && (subfiles = file.listFiles()) != null && subfiles.length == 1 && subfiles[0].isDirectory()) {
            file = subfiles[0];
            filenameBuilder.append('.');
            filenameBuilder.append(file.getName());
        }

        this.path = file.getPath();
        this.indentation = parent.indentation + 1;
        this.x = indentation * master.getIndentPerLevel() + master.getInitialIndent();

        this.filename = filenameBuilder.toString();
        this.isDirectory = file.isDirectory();

        if(!this.isDirectory) {
            if(filename.endsWith(".craftr") || filename.endsWith(".png")) filename = filename.substring(0, filename.lastIndexOf('.'));
        }

        this.addThemeListener();

        if(toOpen.contains(this.path)) {
            expand(toOpen);
        }
    }

    private static boolean canFlatten(File file) {
        Project project = ProjectManager.getAssociatedProject(file);
        return project == null || project.canFlatten(file);
    }

    private void addThemeListener() {
        ThemeChangeListener.addThemeChangeListener(t -> {
            boolean useFileIcon = false;
            if(path.endsWith(".png")) {
                try {
                    useFileIcon = true;
                    File pathToFile = new File(path);
                    this.icon = ImageIO.read(pathToFile).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
                } catch (IOException ex) {
                    useFileIcon = false;
                }
            }
            if(!useFileIcon) {
                String iconName = ProjectManager.getIconFor(new File(path));
                if (iconName == null) {
                    if (this.isDirectory) {
                        if (new File(this.path).getParent().equals(Preferences.get("workspace_dir"))) {
                            iconName = "project";
                        } else {
                            iconName = "package";
                        }
                    } else {
                        iconName = "file";
                    }
                }

                this.icon = Commons.getIcon(iconName).getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            }
        });
    }

    private void expand(ArrayList<String> toOpen) {
        File[] subfiles = new File(path).listFiles();
        if(subfiles == null) return;

        ArrayList<File> subfiles1 = new ArrayList<>();

        for(File f : subfiles) {
            if(f.isDirectory()) {
                this.children.add(new ExplorerItem(this, f, toOpen));
            } else {
                subfiles1.add(f);
            }
        }
        for(File f : subfiles1) {
            this.children.add(new ExplorerItem(this, f, toOpen));
        }

        expanded = true;
        master.getExpandedElements().add(this.path);
        master.repaint();
    }

    private void collapse() {
        this.propagateCollapse();
        this.children.clear();
        expanded = false;
        master.repaint();
    }

    private void propagateCollapse() {
        master.getExpandedElements().remove(this.path);
        for(ExplorerElement element : children) {
            if(element instanceof ExplorerItem) ((ExplorerItem) element).propagateCollapse();
        }
    }

    public void render(Graphics g) {
        int y = master.getOffsetY();
        master.getFlatList().add(this);

        int x = (indentation * master.getIndentPerLevel()) + master.getInitialIndent();

        g.setColor((this.rollover || this.selected) ? master.getColors().get("item.rollover.background") : master.getColors().get("item.background"));
        g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getRowHeight());
        if(this.selected) {
            g.setColor(master.getColors().get("item.selected.background"));

            switch(master.getSelectionStyle()) {
                case "FULL": {
                    g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getRowHeight());
                    break;
                }
                case "LINE_LEFT": {
                    g.fillRect(0, master.getOffsetY(), master.getSelectionLineThickness(), master.getRowHeight());
                    break;
                }
                case "LINE_RIGHT": {
                    g.fillRect(master.getWidth() - master.getSelectionLineThickness(), master.getOffsetY(), master.getSelectionLineThickness(), master.getRowHeight());
                    break;
                }
                case "LINE_TOP": {
                    g.fillRect(0, master.getOffsetY(), master.getWidth(), master.getSelectionLineThickness());
                    break;
                }
                case "LINE_BOTTOM": {
                    g.fillRect(0, master.getOffsetY() + master.getRowHeight() - master.getSelectionLineThickness(), master.getWidth(), master.getSelectionLineThickness());
                    break;
                }
            }
        }

        //Expand/Collapse button
        File[] listFiles = new File(path).listFiles();
        if(isDirectory && listFiles != null && listFiles.length > 0){
            int margin = ((master.getRowHeight() - 16) / 2);
            if(expanded) {
                g.drawImage(master.getAssets().get("collapse"),x,y + margin,16, 16,new Color(0,0,0,0),null);
            } else {
                g.drawImage(master.getAssets().get("expand"),x,y + margin,16, 16,new Color(0,0,0,0),null);
            }
        }
        x += 23;

        //File Icon
        {
            int margin = ((master.getRowHeight() - 16) / 2);
            g.drawImage(this.icon,x,y + margin,16, 16,new Color(0,0,0,0),null);
        }
        x += 25;

        //File Name

        if(this.selected) {
            g.setColor(master.getColors().get("item.selected.foreground"));
        } else if(this.rollover) {
            g.setColor(master.getColors().get("item.rollover.foreground"));
        } else {
            g.setColor(master.getColors().get("item.foreground"));
        }
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString(filename, x, master.getOffsetY() + metrics.getAscent() + ((master.getRowHeight() - metrics.getHeight())/2));
        x += metrics.stringWidth(filename);

        master.setOffsetY(master.getOffsetY() + master.getRowHeight());
        master.setContentWidth(Math.max(master.getWidth(), x));
        for(ExplorerElement i : children) {
            i.render(g);
        }
    }

    private void open() {
        File file = new File(this.path);
        if (file.isDirectory()) {
            if(expanded) collapse();
            else expand(new ArrayList<>());
        } else {
            TabManager.openTab(this.path);
        }
    }

    @Override
    public int getHeight() {
        return master.getRowHeight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown() && e.getClickCount() % 2 == 0 && (e.getX() < x || e.getX() > x + master.getRowHeight())) {
            this.open();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            //x = indentation * master.getIndentPerLevel() + master.getInitialIndent();
            if(this.isDirectory && e.getX() >= x && e.getX() <= x + 20) {
                if(expanded) collapse();
                else expand(new ArrayList<>());
            } else {
                master.setSelected(this, e);
            }
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            if(!this.selected) master.setSelected(this, new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), 0, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), MouseEvent.BUTTON1));
            StyledPopupMenu menu = this.generatePopup();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public String getPath() {
        return path;
    }

    private StyledPopupMenu generatePopup() {
        StyledPopupMenu menu = new StyledPopupMenu();

        String newPath;
        if(this.isDirectory) newPath = this.path;
        else if(this.parent != null) newPath = this.parent.path;
        else newPath = new File(this.path).getParent();

        List<String> selectedFiles = master.getSelectedFiles();

        {
            StyledMenu newMenu = new StyledMenu("New");

            menu.add(newMenu);

            // --------------------------------------------------

            Project project = ProjectManager.getAssociatedProject(new File(path));

            String projectDir = (project != null) ? project.getDirectory().getPath() + File.separator : null;

            if(projectDir != null && (path + File.separator).startsWith(projectDir + "src" + File.separator)) {

                StyledMenuItem entityItem = new StyledMenuItem("Entity", "entity");
                entityItem.addActionListener(e -> FileType.ENTITY.create(newPath));

                newMenu.add(entityItem);

                // --------------------------------------------------

                StyledMenuItem itemItem = new StyledMenuItem("Item", "item");
                itemItem.addActionListener(e -> FileType.ITEM.create(newPath));

                newMenu.add(itemItem);

                // --------------------------------------------------

                StyledMenuItem classItem = new StyledMenuItem("Class", "class");
                classItem.addActionListener(e -> FileType.CLASS.create(newPath));

                newMenu.add(classItem);

                // --------------------------------------------------

                StyledMenuItem featureItem = new StyledMenuItem("Feature", "feature");
                featureItem.addActionListener(e -> FileType.FEATURE.create(newPath));

                newMenu.add(featureItem);

                // --------------------------------------------------

                newMenu.addSeparator();

                // --------------------------------------------------

                StyledMenuItem worldItem = new StyledMenuItem("World", "world");
                worldItem.addActionListener(e -> FileType.WORLD.create(newPath));

                newMenu.add(worldItem);

            } else if(projectDir != null && (path + File.separator).startsWith(projectDir + "resources" + File.separator)) {

                // --------------------------------------------------

                StyledMenuItem modelItem = new StyledMenuItem("Model", "model");
                modelItem.addActionListener(e -> FileType.MODEL.create(newPath));

                newMenu.add(modelItem);

                // --------------------------------------------------

                StyledMenuItem langItem = new StyledMenuItem("Language File", "lang");
                langItem.addActionListener(e -> FileType.LANG.create(newPath));

                newMenu.add(langItem);

                // --------------------------------------------------

                StyledMenuItem mcmetaItem = new StyledMenuItem("META File", "meta");
                mcmetaItem.addActionListener(e -> FileType.META.create(newPath));

                newMenu.add(mcmetaItem);

                // --------------------------------------------------
            }

            // --------------------------------------------------

            newMenu.addSeparator();

            // --------------------------------------------------

            StyledMenuItem packageItem = new StyledMenuItem("Package", "package");
            packageItem.addActionListener(e -> FileType.PACKAGE.create(newPath));

            newMenu.add(packageItem);

        }
        menu.addSeparator();


        menu.add(MenuItems.fileItem(MenuItems.FileMenuItem.COPY));
        menu.add(MenuItems.fileItem(MenuItems.FileMenuItem.PASTE));

        StyledMenuItem deleteItem = MenuItems.fileItem(MenuItems.FileMenuItem.DELETE);
        deleteItem.setEnabled(selectedFiles.size() >= 1);
        deleteItem.addActionListener(e -> FileManager.delete(selectedFiles));
        menu.add(deleteItem);

        menu.addSeparator();
        StyledMenu refactorMenu = new StyledMenu("Refactor");
        menu.add(refactorMenu);

        StyledMenuItem renameItem = MenuItems.fileItem(MenuItems.FileMenuItem.RENAME);
        /*renameItem.addActionListener(e -> {
            if(ExplorerMaster.selectedLabels.size() != 1) return;

            String path = ExplorerMaster.selectedLabels.get(0).parent.path;
            String name = new File(path).getName();
            String rawName = StringUtil.stripExtension(name);
            final String extension = name.replaceAll(rawName, "");
            final String pathToParent = path.substring(0, path.lastIndexOf(name));

            String newName = StringPrompt.prompt("Rename", "Enter a new name for the file:", rawName,
                    new StringValidator() {
                        @Override
                        public boolean validate(String str) {
                            return str.trim().length() > 0 && FileUtil.validateFilename(str)
                                    && !new File(pathToParent + str + extension).exists();
                        }
                    });

            if (newName != null) {
                if (ProjectManager.renameFile(new File(path), newName)) {
                    com.energyxxer.craftr.ui.projectExplorer.ExplorerItem parentItem = ExplorerMaster.selectedLabels.get(0).parent;
                    parentItem.path = pathToParent + newName + extension;
                    if (parentItem.parent != null) {
                        parentItem.parent.collapse();
                        parentItem.parent.refresh();
                    } else {
                        CraftrWindow.projectExplorer.refresh();
                    }

                    TabManager.renameTab(path, pathToParent + newName + extension);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "<html>The action can't be completed because the folder or file is open in another program.<br>Close the folder and try again.</html>",
                            "An error occurred.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });*/
        refactorMenu.add(renameItem);
        refactorMenu.add(MenuItems.fileItem(MenuItems.FileMenuItem.MOVE));

        menu.addSeparator();

        StyledMenuItem openInSystemItem = new StyledMenuItem("Show in System Explorer", "explorer");
        openInSystemItem.addActionListener(e -> Commons.showInExplorer(this.path));

        menu.add(openInSystemItem);

        return menu;
    }
}
