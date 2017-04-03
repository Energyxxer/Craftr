package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.components.menu.XMenuItem;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

/**
 * Menu item that reacts to window theme changes.
 */
public class StyledMenuItem extends XMenuItem {
    public StyledMenuItem(String text, String icon) {
        if(text != null) setText(text);
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setRolloverBackground(t.getColor("General.menu.selected.background",new Color(190, 190, 190)));
            this.setForeground(t.getColor("General.menu.foreground",t.getColor("General.foreground", Color.BLACK)));
            this.setFont(new Font(t.getString("General.menu.font","General.font","default:Tahoma"), 0, 12));
            if(icon != null) this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        });
    }
    public StyledMenuItem(String text) {
        this(text, null);
    }
    public StyledMenuItem() {this(null,null);}
}