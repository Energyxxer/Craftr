package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.components.menu.XMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

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
            this.setBackground(t.getColor(new Color(215, 215, 215), "General.menu.background"));
            this.setForeground(t.getColor(Color.BLACK, "General.menu.foreground","General.foreground"));
            this.setRolloverBackground(t.getColor(new Color(190, 190, 190), "General.menu.selected.background"));
            this.getPopupMenu().setBackground(this.getBackground());
            //this.getPopupMenu().setForeground(this.getForeground());
            int borderThickness = Math.max(t.getInteger(1,"General.menu.border.thickness"),0);
            this.getPopupMenu().setBorder(BorderFactory.createMatteBorder(borderThickness,borderThickness,borderThickness,borderThickness,t.getColor(new Color(200, 200, 200), "General.menu.border.color")));
            this.setFont(new Font(t.getString("General.menu.font","General.font","default:Tahoma"), 0, 12));
            if(icon != null) this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        });
    }
    public StyledMenu(String text) {
        this(text, null);
    }
    public void addSeparator() {
        this.add(new StyledSeparator());
    }
}