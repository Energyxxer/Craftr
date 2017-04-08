package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.global.Status;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.craftr.ui.theme.ThemeManager;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by User on 12/15/2016.
 */
public class StatusBar extends JPanel {

    private StyledLabel statusLabel;

    private Status currentStatus = null;

    private ExtendedStatusBar extension = new ExtendedStatusBar();

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1, 25));

        ThemeChangeListener.addThemeChangeListener(t ->
            SwingUtilities.invokeLater(() -> {
                this.setBackground(t.getColor(new Color(235, 235, 235), "Status.background"));
                this.setBorder(
                    new CompoundBorder(
                        new MatteBorder(Math.max(t.getInteger("Status.border.thickness"),0), 0, 0, 0, t.getColor(new Color(200, 200, 200), "Status.border.color")),
                        new EmptyBorder(0,5,0,5)
                ));}
            )
        );
        statusLabel = new StyledLabel("");
        statusLabel.setIconName("info");
        this.add(statusLabel,BorderLayout.CENTER);

        this.add(extension,BorderLayout.EAST);



        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK), "reloadThemes");
        this.getActionMap().put("reloadThemes", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThemeManager.loadAll();
            }
        });
    }

    public void setStatus(String text) {
        setStatus(new Status(text));
    }

    public void setStatus(Status status) {

        Theme t = CraftrWindow.getTheme();

        statusLabel.setForeground(t.getColor(Color.BLACK, "Status." + status.getType().toLowerCase(),"General.foreground"));
        statusLabel.setText(status.getMessage());

        this.currentStatus = status;
    }

    public void dismissStatus(Status status) {
        if(status == currentStatus) {
            statusLabel.setText("");
            statusLabel.setBackground(new Color(0,0,0,0));
        }
    }

    public void setCaretInfo(String text) {
        extension.setCaretInfo(text);
    }

    public void setSelectionInfo(String text) {
        extension.setSelectionInfo(text);
    }
}
