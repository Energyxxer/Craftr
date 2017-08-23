package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.scrollbar.InvisibleScrollPaneLayout;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Created by User on 12/15/2016.
 */
public class EditArea extends JPanel {

    private static final boolean useConsole = true;

    private JPanel tabList;

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(500, 500));
        ThemeChangeListener.addThemeChangeListener(t -> this.setBackground(t.getColor(new Color(215, 215, 215), "Editor.background")));

        JPanel tabListHolder = new JPanel(new BorderLayout());
        ThemeChangeListener.addThemeChangeListener(t -> {
            tabListHolder.setBackground(t.getColor(new Color(200, 202, 205), "TabList.background"));
            tabListHolder.setPreferredSize(new Dimension(1, t.getInteger(30, "TabList.height")));
        });

        JPanel tabActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        tabActionPanel.setOpaque(false);
        ThemeChangeListener.addThemeChangeListener(t -> tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"TabList.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "TabList.border.color"))));

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
        ThemeChangeListener.addThemeChangeListener(t -> {
            tabList.setBackground(t.getColor(new Color(200, 202, 205), "TabList.background"));
            tabList.setPreferredSize(new Dimension(1, t.getInteger(30, "TabList.height")));
            tabList.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"TabList.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "TabList.border.color")));
        });

        JScrollPane tabSP = new JScrollPane(CraftrWindow.tabList);
        tabSP.setBorder(BorderFactory.createEmptyBorder());
        tabSP.setLayout(new InvisibleScrollPaneLayout(tabSP));
        tabSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tabListHolder.add(tabSP, BorderLayout.CENTER);

        if (useConsole) this.add(new ConsoleArea(), BorderLayout.SOUTH);
        this.add(CraftrWindow.noticeBoard = new NoticeBoard(), BorderLayout.SOUTH);
    }
}
