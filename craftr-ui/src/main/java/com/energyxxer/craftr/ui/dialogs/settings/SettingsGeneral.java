package com.energyxxer.craftr.ui.dialogs.settings;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.styledcomponents.StyledTextField;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created by User on 1/21/2017.
 */
class SettingsGeneral extends JPanel {

    {
        {
            JPanel header = new JPanel(new BorderLayout());
            header.setPreferredSize(new Dimension(0,40));
            this.add(header, BorderLayout.NORTH);

            {
                JPanel padding = new JPanel();
                padding.setOpaque(false);
                padding.setPreferredSize(new Dimension(25,25));
                header.add(padding, BorderLayout.WEST);
            }

            JLabel label = new JLabel("General");
            header.add(label, BorderLayout.CENTER);

            ThemeChangeListener.addThemeChangeListener(t -> {
                setBackground(t.getColor(new Color(235, 235, 235), "Settings.content.background"));
                header.setBackground(t.getColor(new Color(235, 235, 235), "Settings.content.header.background"));
                header.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"Settings.content.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "Settings.content.header.border.color")));
                label.setForeground(t.getColor(Color.BLACK, "Settings.content.header.foreground"));
                label.setFont(new Font(t.getString("Settings.content.header.font","General.font","default:Tahoma"),1,20));
            });
        }

        {
            JPanel padding_left = new JPanel();
            padding_left.setOpaque(false);
            padding_left.setPreferredSize(new Dimension(50,25));
            this.add(padding_left, BorderLayout.WEST);
        }
        {
            JPanel padding_right = new JPanel();
            padding_right.setOpaque(false);
            padding_right.setPreferredSize(new Dimension(50,25));
            this.add(padding_right, BorderLayout.EAST);
        }

        {

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);
            this.add(content, BorderLayout.CENTER);

            {
                JPanel padding = new JPanel();
                padding.setOpaque(false);
                padding.setMinimumSize(new Dimension(1,20));
                padding.setMaximumSize(new Dimension(1,20));
                content.add(padding);
            }

            {
                StyledLabel label = new StyledLabel("Display name:","Settings.content");
                label.setStyle(Font.BOLD);
                content.add(label);
            }

            {
                StyledTextField nameField = new StyledTextField(Preferences.get("username","User"),"Settings.content");
                nameField.setMaximumSize(new Dimension(300,25));
                nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
                Settings.addOpenEvent(() -> nameField.setText(Preferences.get("username","User")));
                Settings.addApplyEvent(() -> {
                    String text = nameField.getText();
                    if(text.trim().length() > 0)
                        Preferences.put("username",nameField.getText().trim());
                });
                content.add(nameField);
            }
        }
    }

    public SettingsGeneral() {
        super(new BorderLayout());
    }
}
