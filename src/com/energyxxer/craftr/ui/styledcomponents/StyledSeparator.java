package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Separator that reacts to window theme changes.
 */
public class StyledSeparator extends JSeparator {

    private String namespace = null;

    public StyledSeparator() {
        this(null);
    }

    public StyledSeparator(String namespace) {
        if(namespace != null) setNamespace(namespace);
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                this.setForeground(t.getColor(this.namespace + ".menu.separator", t.getColor("General.menu.separator",new Color(150, 150, 150))));
            } else {
                this.setForeground(t.getColor("General.menu.separator",new Color(150, 150, 150)));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }
}