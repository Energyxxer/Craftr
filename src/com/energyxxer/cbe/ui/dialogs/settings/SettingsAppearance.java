package com.energyxxer.cbe.ui.dialogs.settings;

import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.components.XDropdownMenu;
import com.energyxxer.cbe.ui.styledcomponents.StyledLabel;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.ThemeManager;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created by User on 1/21/2017.
 */
class SettingsAppearance extends JPanel {

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

            JLabel label = new JLabel("Appearance");
            header.add(label, BorderLayout.CENTER);

            ThemeChangeListener.addThemeChangeListener(t -> {
                setBackground(t.getColor("Settings.content.background", new Color(235, 235, 235)));
                header.setBackground(t.getColor("Settings.content.header.background", new Color(235, 235, 235)));
                header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("Settings.content.header.border", new Color(200, 200, 200))));
                label.setForeground(t.getColor("Settings.content.header.foreground", Color.BLACK));
                label.setFont(new Font(t.getString("Settings.content.header.font",t.getString("General.font","Tahoma")),1,20));
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
                StyledLabel label = new StyledLabel("Color Scheme:","Settings.content");
                label.setStyle(Font.BOLD);
                content.add(label);
            }
            {
                XDropdownMenu<Theme> themeDropdown = new XDropdownMenu<>(ThemeManager.getThemesAsArray());
                themeDropdown.setPopupFactory(StyledPopupMenu::new);
                themeDropdown.setPopupItemFactory(StyledMenuItem::new);
                themeDropdown.setValue(Window.getTheme());
                Settings.addOpenEvent(ThemeManager::loadAll);
                Settings.addApplyEvent(() -> ThemeManager.setTheme(themeDropdown.getValue().getName()));
                content.add(themeDropdown);
            }

        }
    }

    public SettingsAppearance() {
        super(new BorderLayout());
    }
}
