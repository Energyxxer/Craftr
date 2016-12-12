package com.energyxxer.cbe.ui.components.themechange;

import com.energyxxer.cbe.ui.components.menu.XMenu;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.*;
import java.awt.*;

/**
 * Separator that reacts to window theme changes.
 */
public class TCMenu extends XMenu {

    static {
        UIManager.put("Menu.submenuPopupOffsetX",0);
        UIManager.put("Menu.submenuPopupOffsetY",-1);
    }
    public TCMenu(String text, String icon) {
        super(text);

        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setRolloverBackground(t.h2);
            this.setBackground(t.p3);
            this.setForeground(t.t1);
            this.getPopupMenu().setBackground(t.p3);
            this.getPopupMenu().setForeground(t.p1);
            this.getPopupMenu().setBorderPainted(true);
            this.getPopupMenu().setBorder(BorderFactory.createMatteBorder(1,1,1,1,t.l1));
            this.setFont(new Font(t.font1, 0, 12));
            if(icon != null) this.setIcon(new ImageIcon(ImageManager.load(String.format("/assets/icons/%s%s.png", t.path, icon)).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        });
    }
    public TCMenu(String text) {
        this(text, null);
    }
    public void addSeparator() {
        this.add(new TCSeparator());
    }
}