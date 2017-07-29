package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.NoticeExplorerMaster;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollBarUI;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Created by User on 5/16/2017.
 */
public class NoticeBoard extends JPanel {

    private static final int BOARD_HEIGHT = 300;

    public final NoticeExplorerMaster explorerMaster;

    public void expand() {
        this.setPreferredSize(new Dimension(0, BOARD_HEIGHT));
        this.revalidate();
        this.repaint();
    }

    public void collapse() {
        this.setPreferredSize(new Dimension(0, 25));
        this.revalidate();
        this.repaint();
    }

    public NoticeBoard() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, 25));
        ThemeChangeListener.addThemeChangeListener(t -> this.setBorder(BorderFactory.createMatteBorder(Math.max(t.getInteger(1, "NoticeBoard.header.border.thickness"),0), 0, 0, 0, t.getColor(new Color(200, 200, 200), "NoticeBoard.header.border.color"))));

        JPanel boardHeader = new JPanel(new BorderLayout());
        ThemeChangeListener.addThemeChangeListener(t -> boardHeader.setBackground(t.getColor(new Color(235, 235, 235), "NoticeBoard.header.background")));
        boardHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        boardHeader.setPreferredSize(new Dimension(0, 25));

        StyledLabel boardLabel = new StyledLabel("Notice Board", "NoticeBoard.header");
        boardHeader.add(boardLabel, BorderLayout.WEST);

        JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        consoleActionPanel.setOpaque(false);

        ToolbarButton toggle = new ToolbarButton("toggle", true);
        toggle.setToolTipText("Toggle Board");
        toggle.setPreferredSize(new Dimension(20,20));

        toggle.addActionListener(e -> {
            if (this.getPreferredSize().height == 25) {
                expand();
            } else {
                collapse();
            }
        });

        ToolbarButton clear = new ToolbarButton("clear", true);
        clear.setToolTipText("Clear Board");
        clear.setPreferredSize(new Dimension(20,20));

        consoleActionPanel.add(clear);
        consoleActionPanel.add(toggle);
        boardHeader.add(consoleActionPanel, BorderLayout.EAST);

        this.add(boardHeader, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(this.explorerMaster = CraftrWindow.noticeExplorer = new NoticeExplorerMaster());
        sp.setBorder(new EmptyBorder(0,0,0,0));
        sp.setLayout(new OverlayScrollPaneLayout(sp));

        this.add(sp, BorderLayout.CENTER);

        clear.addActionListener(e -> explorerMaster.clear());
    }
}
