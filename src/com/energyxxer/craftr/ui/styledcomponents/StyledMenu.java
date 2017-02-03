package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.components.menu.XMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.ImageManager;

import javax.swing.*;
import java.awt.*;

/**
 * Separator that reacts to window theme changes.
 */
public class StyledMenu extends XMenu {

    static {
        UIManager.put("Menu.submenuPopupOffsetX",0);
        UIManager.put("Menu.submenuPopupOffsetY",-1);
    }
    public StyledMenu(String text, String icon) {
        super(text);

        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor("General.menu.background",new Color(215, 215, 215)));
            this.setForeground(t.getColor("General.menu.foreground",t.getColor("General.foreground", Color.BLACK)));
            this.setRolloverBackground(t.getColor("General.menu.selected.background",new Color(190, 190, 190)));
            this.getPopupMenu().setBackground(this.getBackground());
            //this.getPopupMenu().setForeground(this.getForeground());
            this.getPopupMenu().setBorder(BorderFactory.createMatteBorder(1,1,1,1,t.getColor("General.menu.border",new Color(200, 200, 200))));
            this.setFont(new Font(t.getString("General.menu.font",t.getString("General.font","Tahoma")), 0, 12));
            if(icon != null) this.setIcon(new ImageIcon(ImageManager.load(String.format("/assets/icons/%s%s.png", Commons.themeAssetsPath, icon)).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        });
    }
    public StyledMenu(String text) {
        this(text, null);
    }
    public void addSeparator() {
        this.add(new StyledSeparator());
    }
}