package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.ExplorerMaster;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollBarUI;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.styledcomponents.Padding;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

/**
 * Created by User on 12/15/2016.
 */
public class Sidebar extends JPanel {

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350, 500));
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor(Color.WHITE, "Explorer.background"));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, Math.max(t.getInteger(1,"Explorer.border.thickness"), 0), t.getColor(new Color(200, 200, 200), "Explorer.border.color")));
        });

        JPanel header = new JPanel(new BorderLayout());

        StyledLabel label = new StyledLabel("Project Explorer", "Explorer.header");
        label.setFontSize(14);
        label.setPreferredSize(new Dimension(500, 25));
        header.add(new Padding(15, "Explorer.header.indent"), BorderLayout.WEST);
        header.add(label, BorderLayout.CENTER);

        ThemeChangeListener.addThemeChangeListener(t -> {
            header.setBackground(t.getColor(this.getBackground(),"Explorer.header.background"));
            header.setPreferredSize(new Dimension(500, t.getInteger(25, "Explorer.header.height")));
            label.setPreferredSize(new Dimension(500, t.getInteger(25, "Explorer.header.height")));
        });

        ToolbarButton refresh = new ToolbarButton("reload");
        refresh.setToolTipText("Refresh Explorer");

        refresh.addActionListener(e -> CraftrWindow.explorer.refresh());

        header.add(refresh, BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(CraftrWindow.explorer = new ExplorerMaster(new File(Preferences.get("workspace_dir"))));
        sp.setBorder(new EmptyBorder(0,0,0,0));
        sp.getVerticalScrollBar().setUI(new OverlayScrollBarUI(sp, 20));
        sp.getHorizontalScrollBar().setUI(new OverlayScrollBarUI(sp, 20));
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.getHorizontalScrollBar().setUnitIncrement(20);
        sp.getVerticalScrollBar().setOpaque(false);
        sp.getHorizontalScrollBar().setOpaque(false);
        sp.setLayout(new OverlayScrollPaneLayout());

        sp.setComponentZOrder(sp.getVerticalScrollBar(), 0);
        sp.setComponentZOrder(sp.getHorizontalScrollBar(), 1);
        sp.setComponentZOrder(sp.getViewport(), 2);

        this.add(sp, BorderLayout.CENTER);
    }
}
