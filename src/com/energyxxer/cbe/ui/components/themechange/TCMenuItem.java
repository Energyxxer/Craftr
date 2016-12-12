package com.energyxxer.cbe.ui.components.themechange;

import com.energyxxer.cbe.ui.components.menu.XMenuItem;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.*;
import java.awt.*;

/**
 * Menu item that reacts to window theme changes.
 */
public class TCMenuItem extends XMenuItem {
    public TCMenuItem(String text, String icon) {
        super(text);
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setRolloverBackground(t.h2);
            this.setForeground(t.t1);
            this.setFont(new Font(t.font1, 0, 12));
            if(icon != null) this.setIcon(new ImageIcon(ImageManager.load(String.format("/assets/icons/%s%s.png", t.path, icon)).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        });
    }
    public TCMenuItem(String text) {
        this(text, null);
    }
}