package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.NoticeExplorerMaster;
import com.energyxxer.craftr.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.xswing.hints.Hint;

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

    private ThemeListenerManager tlm = new ThemeListenerManager();

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
        tlm.addThemeChangeListener(t -> this.setBorder(BorderFactory.createMatteBorder(Math.max(t.getInteger(1, "NoticeBoard.header.border.thickness"),0), 0, 0, 0, t.getColor(new Color(200, 200, 200), "NoticeBoard.header.border.color"))));

        JPanel boardHeader = new JPanel(new BorderLayout());
        tlm.addThemeChangeListener(t -> boardHeader.setBackground(t.getColor(new Color(235, 235, 235), "NoticeBoard.header.background")));
        boardHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        boardHeader.setPreferredSize(new Dimension(0, 25));

        StyledLabel boardLabel = new StyledLabel("Notice Board", "NoticeBoard.header");
        boardHeader.add(boardLabel, BorderLayout.WEST);

        JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        consoleActionPanel.setOpaque(false);

        ToolbarButton toggle = new ToolbarButton("toggle", tlm);
        toggle.setHintText("Toggle Board");
        toggle.setPreferredHintPos(Hint.LEFT);
        toggle.setPreferredSize(new Dimension(20,20));

        toggle.addActionListener(e -> {
            if (this.getPreferredSize().height == 25) {
                expand();
            } else {
                collapse();
            }
        });

        ToolbarButton clear = new ToolbarButton("clear", tlm);
        clear.setHintText("Clear Board");
        clear.setPreferredHintPos(Hint.LEFT);
        clear.setPreferredSize(new Dimension(20,20));

        consoleActionPanel.add(clear);
        consoleActionPanel.add(toggle);
        boardHeader.add(consoleActionPanel, BorderLayout.EAST);

        this.add(boardHeader, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(CraftrWindow.noticeExplorer = new NoticeExplorerMaster());
        sp.setBorder(new EmptyBorder(0,0,0,0));
        sp.setLayout(new OverlayScrollPaneLayout(sp));

        this.add(sp, BorderLayout.CENTER);

        clear.addActionListener(e -> CraftrWindow.noticeExplorer.clear());
    }
}
