package com.energyxxer.craftr.ui.dialogs;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.styledcomponents.StyledButton;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.xswing.Padding;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by User on 2/11/2017.
 */
public class ConfirmDialog {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 110;

    public boolean result = false;

    private ThemeListenerManager tlm = new ThemeListenerManager();

    public ConfirmDialog(String title, String query) {
        JDialog dialog = new JDialog(CraftrWindow.jframe);

        JPanel pane = new JPanel(new BorderLayout());
        pane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        tlm.addThemeChangeListener(t -> pane.setBackground(t.getColor(new Color(235, 235, 235), "ConfirmDialog.background")));

        pane.add(new Padding(10), BorderLayout.NORTH);
        pane.add(new Padding(25), BorderLayout.WEST);
        pane.add(new Padding(25), BorderLayout.EAST);
        pane.add(new Padding(10), BorderLayout.SOUTH);

        {
            JPanel content = new JPanel(new BorderLayout());
            content.setOpaque(false);

            StyledLabel label = new StyledLabel(query, "ConfirmDialog");
            label.setStyle(Font.BOLD);
            content.add(label, BorderLayout.CENTER);

            {
                JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttons.setOpaque(false);
                buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

                StyledButton okButton = new StyledButton("Yes");
                okButton.addActionListener(e -> {
                    result = true;
                    dialog.setVisible(false);
                });
                buttons.add(okButton);

                StyledButton cancelButton = new StyledButton("No");
                cancelButton.addActionListener(e -> {
                    result = false;
                    dialog.setVisible(false);
                });

                buttons.add(cancelButton);
                content.add(buttons, BorderLayout.SOUTH);
            }

            pane.add(content, BorderLayout.CENTER);
        }

        pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submit");
        pane.getActionMap().put("submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = true;
                dialog.setVisible(false);
                tlm.dispose();
                dialog.dispose();
            }
        });

        dialog.setContentPane(pane);
        dialog.pack();

        dialog.setTitle(title);

        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.x -= dialog.getWidth()/2;
        center.y -= dialog.getHeight()/2;

        dialog.setLocation(center);

        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);

        dialog.setVisible(true);
    }
}
