package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.ProjectExplorerMaster;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.styledcomponents.Padding;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
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

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350, 500));
        tlm.addThemeChangeListener(t -> {
            this.setBackground(t.getColor(Color.WHITE, "Explorer.background"));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, Math.max(t.getInteger(1, "Explorer.border.thickness"), 0), t.getColor(new Color(200, 200, 200), "Explorer.border.color")));
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

            configure.addActionListener(e -> CraftrWindow.projectExplorer.refresh());

            buttonPanel.add(configure);
        }

        {
            ToolbarButton collapse = new ToolbarButton("arrow_left", tlm);
            collapse.setHintText("Collapse Explorer");

            collapse.addActionListener(e -> CraftrWindow.projectExplorer.refresh());

            buttonPanel.add(collapse);
        }


        this.add(header, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(CraftrWindow.projectExplorer = new ProjectExplorerMaster(new File(Preferences.get("workspace_dir"))));
        sp.setBorder(new EmptyBorder(0,0,0,0));
        sp.setLayout(new OverlayScrollPaneLayout(sp));

        this.add(sp, BorderLayout.CENTER);
    }
}
