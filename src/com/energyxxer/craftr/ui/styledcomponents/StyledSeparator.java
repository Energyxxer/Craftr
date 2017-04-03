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
                this.setForeground(t.getColor(new Color(150, 150, 150), this.namespace + ".menu.separator","General.menu.separator"));
            } else {
                this.setForeground(t.getColor(new Color(150, 150, 150), "General.menu.separator"));
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