package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.global.Status;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.styledcomponents.StyledLabel;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

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
                this.setBackground(t.getColor("Status.background",new Color(235, 235, 235)));
                this.setBorder(
                    new CompoundBorder(
                        new MatteBorder(1, 0, 0, 0, t.getColor("Status.border",new Color(200, 200, 200))),
                        new EmptyBorder(0,5,0,5)
                ));}
            )
        );
        statusLabel = new StyledLabel("");
        statusLabel.setIconName("info");
        this.add(statusLabel,BorderLayout.CENTER);

        this.add(extension,BorderLayout.EAST);
    }

    public void setStatus(String text) {
        setStatus(new Status(text));
    }

    public void setStatus(Status status) {

        Theme t = Window.getTheme();

        statusLabel.setForeground(t.getColor("Status." + status.getType().toLowerCase(),t.getColor("General.foreground",Color.BLACK)));
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
