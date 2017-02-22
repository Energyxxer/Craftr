package com.energyxxer.craftr.ui.explorer;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.FileManager;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.logic.Project;
import com.energyxxer.craftr.ui.common.MenuItems;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenu;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.craftr.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.sun.istack.internal.NotNull;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/7/2017.
 */
public class ExplorerItem implements MouseListener {
    private final ExplorerMaster master;

    public ExplorerItem parent = null;
    protected String path = null;
    private boolean isDirectory = false;
    private String filename = null;
    private int indentation = 0;

    private ArrayList<ExplorerItem> children = new ArrayList<>();

    boolean selected = false;
    private boolean expanded = false;

    private Image icon = null;

    private int x = 0;

    ExplorerItem(ExplorerMaster master, File file, ArrayList<String> toOpen) {
        this.path = file.getPath();
        this.master = master;

        this.isDirectory = file.isDirectory();

        this.filename = file.getName();

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
        this.x = indentation * ExplorerMaster.INDENT_IN_PIXELS;

        this.filename = filenameBuilder.toString();

        this.isDirectory = file.isDirectory();

        this.addThemeListener();

        if(toOpen.contains(this.path)) {
            expand(toOpen);
        }
    }

    private static boolean canFlatten(File file) {
        Project project = ProjectManager.getAssociatedProject(file);
        if(project == null) return true;
        return project.canFlatten(file);
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
        master.openDirectories.add(this.path);
        master.repaint();
    }

    private void collapse() {
        this.propagateCollapse();
        this.children.clear();
        expanded = false;
        master.repaint();
    }

    private void propagateCollapse() {
        master.openDirectories.remove(this.path);
        for(ExplorerItem item : children) {
            item.propagateCollapse();
        }
    }

    void render(Graphics g) {

        int y = master.offsetY;
        master.flatList.add(this);

        int x = indentation * ExplorerMaster.INDENT_IN_PIXELS;

        g.setColor(ExplorerMaster.colors.get("item.selected.background"));
        if(this.selected) g.fillRect(0, master.offsetY, master.getWidth(), ExplorerMaster.ROW_HEIGHT);

        //Expand/Collapse button
        File[] listFiles = new File(path).listFiles();
        if(isDirectory && listFiles != null && listFiles.length > 0){
            if(expanded) {
                g.drawImage(ExplorerMaster.assets.get("collapse"),x+2,y+2,16, 16,new Color(0,0,0,0),null);
            } else {
                g.drawImage(ExplorerMaster.assets.get("expand"),x+2,y+2,16, 16,new Color(0,0,0,0),null);
            }
        }
        x += ExplorerMaster.ROW_HEIGHT + 3;

        //File Icon
        {
            g.drawImage(this.icon,x+2,y+2,16, 16,new Color(0,0,0,0),null);
        }
        x += ExplorerMaster.ROW_HEIGHT + 5;

        g.setColor(ExplorerMaster.colors.get("item.foreground"));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString(filename, x, master.offsetY + metrics.getAscent() + ((ExplorerMaster.ROW_HEIGHT - metrics.getHeight())/2));
        x += metrics.stringWidth(filename);

        master.offsetY += ExplorerMaster.ROW_HEIGHT;
        master.width = Math.max(master.width, x);
        for(ExplorerItem i : children) {
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
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() % 2 == 0 && (e.getX() < x || e.getX() > x + ExplorerMaster.ROW_HEIGHT)) {
            this.open();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(this.isDirectory && e.getX() >= x && e.getX() <= x + ExplorerMaster.ROW_HEIGHT) {
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
                    com.energyxxer.craftr.ui.explorer.ExplorerItem parentItem = ExplorerMaster.selectedLabels.get(0).parent;
                    parentItem.path = pathToParent + newName + extension;
                    if (parentItem.parent != null) {
                        parentItem.parent.collapse();
                        parentItem.parent.refresh();
                    } else {
                        Window.explorer.refresh();
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
