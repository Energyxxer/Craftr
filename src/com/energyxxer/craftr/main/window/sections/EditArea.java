package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Created by User on 12/15/2016.
 */
public class EditArea extends JPanel {

    private static final boolean useConsole = true;

    public JPanel tabList;

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(500, 500));
        ThemeChangeListener.addThemeChangeListener(t -> this.setBackground(t.getColor("Editor.background",new Color(215, 215, 215))));

        JPanel tabListHolder = new JPanel(new BorderLayout());
        tabListHolder.setPreferredSize(new Dimension(1,30));
        ThemeChangeListener.addThemeChangeListener(t -> tabListHolder.setBackground(t.getColor("TabList.background",new Color(200, 202, 205))));

        JPanel tabActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        tabActionPanel.setOpaque(false);
        ThemeChangeListener.addThemeChangeListener(t -> tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("TabList.border",new Color(200, 200, 200)))));

        {
            ToolbarButton more = new ToolbarButton("more");
            more.setToolTipText("View all tabs");
            more.setPreferredSize(new Dimension(25,25));
            tabActionPanel.add(more);

            more.addActionListener(e -> TabManager.getMenu().show(more, more.getX(), more.getY() + more.getHeight()));

        }

        tabListHolder.add(tabActionPanel, BorderLayout.EAST);

        this.add(tabListHolder, BorderLayout.NORTH);
        tabList = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabList.setPreferredSize(new Dimension(1, 30));
        ThemeChangeListener.addThemeChangeListener(t -> {
            tabList.setBackground(t.getColor("TabList.background",new Color(200, 202, 205)));
            tabList.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("TabList.border",new Color(200, 200, 200))));
        });
        tabListHolder.add(tabList, BorderLayout.CENTER);

        if (useConsole) this.add(new ConsoleArea(), BorderLayout.SOUTH);
    }
}
