package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.ProjectExplorerMaster;
import com.energyxxer.craftr.ui.explorer.base.ExplorerFlag;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.styledcomponents.Padding;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.craftr.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

/**
 * Created by User on 12/15/2016.
 */
public class Sidebar extends JPanel {

    private ThemeListenerManager tlm = new ThemeListenerManager();

    private JPanel expanded = new JPanel(new BorderLayout());
    private JPanel collapsed = new JPanel(new BorderLayout());

    public Sidebar() {
        super(new BorderLayout());
    }

    {
        expanded.setPreferredSize(new Dimension(350, 5));
        tlm.addThemeChangeListener(t -> {
            expanded.setBackground(t.getColor(Color.WHITE, "Explorer.background"));
            expanded.setBorder(BorderFactory.createMatteBorder(0, 0, 0, Math.max(t.getInteger(1, "Explorer.border.thickness"), 0), t.getColor(new Color(200, 200, 200), "Explorer.border.color")));
        });

        JPanel header = new JPanel(new BorderLayout());

        StyledLabel label = new StyledLabel("Project Explorer", "Explorer.header");
        label.setFontSize(14);
        label.setPreferredSize(new Dimension(500, 25));
        header.add(new Padding(15, "Explorer.header.indent"), BorderLayout.WEST);
        header.add(label, BorderLayout.CENTER);

        tlm.addThemeChangeListener(t -> {
            header.setBackground(t.getColor(this.getBackground(), "Explorer.header.background"));
            header.setPreferredSize(new Dimension(500, t.getInteger(25, "Explorer.header.height")));
            label.setPreferredSize(new Dimension(500, t.getInteger(25, "Explorer.header.height")));
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        ((FlowLayout) buttonPanel.getLayout()).setHgap(2);

        header.add(buttonPanel, BorderLayout.EAST);

        {
            ToolbarButton refresh = new ToolbarButton("reload", tlm);
            refresh.setHintText("Refresh Explorer");

            refresh.addActionListener(e -> CraftrWindow.projectExplorer.refresh());

            buttonPanel.add(refresh);
        }

        {
            ToolbarButton configure = new ToolbarButton("cog_dropdown", tlm);
            configure.setHintText("Configure");

            configure.addActionListener(e -> {
                StyledPopupMenu menu = new StyledPopupMenu("What is supposed to go here?");

                {
                    StyledMenuItem item = new StyledMenuItem("Flatten Empty Packages", "checkmark");
                    item.setIconName(CraftrWindow.projectExplorer.getFlag(ProjectExplorerMaster.FLATTEN_EMPTY_PACKAGES) ? "checkmark" : "blank");

                    item.addActionListener(aa -> {
                        CraftrWindow.projectExplorer.toggleFlag(ProjectExplorerMaster.FLATTEN_EMPTY_PACKAGES);
                        CraftrWindow.projectExplorer.refresh();

                        Preferences.put("explorer.flatten_empty_packages",Boolean.toString(CraftrWindow.projectExplorer.getFlag(ProjectExplorerMaster.FLATTEN_EMPTY_PACKAGES)));
                    });

                    menu.add(item);
                }

                {
                    StyledMenuItem item = new StyledMenuItem("Show Project Files", "checkmark");
                    item.setIconName(CraftrWindow.projectExplorer.getFlag(ProjectExplorerMaster.SHOW_PROJECT_FILES) ? "checkmark" : "blank");

                    item.addActionListener(aa -> {
                        CraftrWindow.projectExplorer.toggleFlag(ProjectExplorerMaster.SHOW_PROJECT_FILES);
                        CraftrWindow.projectExplorer.refresh();

                        Preferences.put("explorer.show_project_files",Boolean.toString(CraftrWindow.projectExplorer.getFlag(ProjectExplorerMaster.SHOW_PROJECT_FILES)));
                    });
                    menu.add(item);
                }

                {
                    StyledMenuItem item = new StyledMenuItem("Debug Width", "checkmark");
                    item.setIconName(CraftrWindow.projectExplorer.getFlag(ExplorerFlag.DEBUG_WIDTH) ? "checkmark" : "blank");

                    item.addActionListener(aa -> {
                        CraftrWindow.projectExplorer.toggleFlag(ExplorerFlag.DEBUG_WIDTH);

                        Preferences.put("explorer.debug_width",Boolean.toString(CraftrWindow.projectExplorer.getFlag(ExplorerFlag.DEBUG_WIDTH)));
                    });
                    menu.add(item);
                }

                menu.show(configure, configure.getWidth()/2, configure.getHeight());

                CraftrWindow.projectExplorer.refresh();
            });

            buttonPanel.add(configure);
        }

        {
            ToolbarButton collapse = new ToolbarButton("arrow_left", tlm);
            collapse.setHintText("Collapse Explorer");

            collapse.addActionListener(e -> collapse());

            buttonPanel.add(collapse);
        }

        expanded.add(header, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(CraftrWindow.projectExplorer = new ProjectExplorerMaster(new File(Preferences.get("workspace_dir"))));
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.setLayout(new OverlayScrollPaneLayout(sp));

        expanded.add(sp, BorderLayout.CENTER);
    }
    {

        collapsed.setPreferredSize(new Dimension(29, 50));
        tlm.addThemeChangeListener(t -> {
            collapsed.setBackground(t.getColor(Color.WHITE, "Explorer.background"));
            collapsed.setBorder(BorderFactory.createMatteBorder(0, 0, 0, Math.max(t.getInteger(1, "Explorer.border.thickness"), 0), t.getColor(new Color(200, 200, 200), "Explorer.border.color")));
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        collapsed.add(buttonPanel, BorderLayout.NORTH);

        {
            ToolbarButton expand = new ToolbarButton("arrow_right", tlm);
            expand.setHintText("Expand Explorer");

            expand.addActionListener(e -> expand());

            buttonPanel.add(expand);
        }

        if(Preferences.get("explorer.expanded", "false").equals("true")) {
            expand();
        } else {
            collapse();
        }
    }

    public void expand() {
        this.removeAll();
        this.add(expanded, BorderLayout.CENTER);
        update();

        Preferences.put("explorer.expanded", "true");
    }

    public void collapse() {
        this.removeAll();
        this.add(collapsed, BorderLayout.CENTER);
        update();

        Preferences.put("explorer.expanded", "false");
    }

    private void update() {
        this.revalidate();
        this.repaint();
    }
}
