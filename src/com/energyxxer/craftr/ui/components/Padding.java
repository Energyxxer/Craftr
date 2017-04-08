package com.energyxxer.craftr.ui.components;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.JPanel;
import java.awt.Dimension;

/**
 * Created by User on 2/11/2017.
 */
public class Padding extends JPanel {

    public Padding(int size) {
        this(size, (String[]) null);
    }

    public Padding(int size, String... keys) {
        this.setOpaque(false);
        if(keys != null && keys.length > 0) {
            ThemeChangeListener.addThemeChangeListener(t -> {
                int realSize = t.getInteger(size, keys);
                Dimension dim = new Dimension(realSize, realSize);
                this.setPreferredSize(dim);
                this.setMaximumSize(dim);
            });
        } else {
            Dimension dim = new Dimension(size, size);
            this.setPreferredSize(dim);
            this.setMaximumSize(dim);
        }

    }
}
