package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.scrollbar.InvisibleScrollPaneLayout;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.xswing.Padding;
import com.energyxxer.xswing.hints.Hint;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/15/2016.
 */
public class EditArea extends JPanel {

    private JPanel tabList;
    
    private ThemeListenerManager tlm = new ThemeListenerManager();

    private JComponent content = null;

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(500, 500));
        tlm.addThemeChangeListener(t -> this.setBackground(t.getColor(new Color(215, 215, 215), "Editor.background")));

        JPanel tabListHolder = new JPanel(new BorderLayout());
        tlm.addThemeChangeListener(t -> {
            tabListHolder.setBackground(t.getColor(new Color(200, 202, 205), "TabList.background"));
            tabListHolder.setPreferredSize(new Dimension(1, t.getInteger(30, "TabList.height")));
        });

        JPanel tabActionPanel = new JPanel(new GridBagLayout());
        tabActionPanel.setOpaque(false);
        tlm.addThemeChangeListener(t -> tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"TabList.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "TabList.border.color"))));

        {
            ToolbarButton more = new ToolbarButton("more", tlm);
            more.setHintText("View all tabs");
            more.setPreferredHintPos(Hint.LEFT);
            more.setPreferredSize(new Dimension(25,25));
            tabActionPanel.add(more);

            more.addActionListener(e -> TabManager.getMenu().show(more, more.getWidth()/2, more.getHeight()));
        }
        tabActionPanel.add(new Padding(5));

        tabListHolder.add(tabActionPanel, BorderLayout.EAST);

        this.add(tabListHolder, BorderLayout.NORTH);

        tabList = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tlm.addThemeChangeListener(t -> {
            tabList.setBackground(t.getColor(new Color(200, 202, 205), "TabList.background"));
            tabList.setPreferredSize(new Dimension(1, t.getInteger(30, "TabList.height")));
            tabList.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"TabList.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "TabList.border.color")));
        });

        JScrollPane tabSP = new JScrollPane(CraftrWindow.tabList);
        tabSP.setBorder(BorderFactory.createEmptyBorder());
        tabSP.setLayout(new InvisibleScrollPaneLayout(tabSP));
        tabSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tabListHolder.add(tabSP, BorderLayout.CENTER);

        this.setContent(CraftrWindow.welcomePane);
    }

    public void setContent(JComponent content) {
        if(this.content != null) {
            if(this.content == CraftrWindow.welcomePane) {
                CraftrWindow.welcomePane.tipScreen.pause();
            }
            this.remove(this.content);
        }
        if(content == null) content = CraftrWindow.welcomePane;

        this.add(content, BorderLayout.CENTER);
        this.content = content;
        if(content == CraftrWindow.welcomePane) {
            if(CraftrWindow.isVisible()) CraftrWindow.welcomePane.tipScreen.start();
        }

        this.revalidate();
        this.repaint();
    }
}
