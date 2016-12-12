package com.energyxxer.cbe.ui.components.themechange;

import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Separator that reacts to window theme changes.
 */
public class TCSeparator extends JSeparator {
    public TCSeparator() {
        super();
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setForeground(t.l2);
            this.setBackground(new Color(0,0,0,0));
        });
    }
}