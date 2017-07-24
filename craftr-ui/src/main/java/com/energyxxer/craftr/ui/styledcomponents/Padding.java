package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import java.awt.Dimension;

/**
 * Created by User on 5/12/2017.
 */
public class Padding extends com.energyxxer.xswing.Padding {
    public Padding(int size, String... keys) {
        ThemeChangeListener.addThemeChangeListener(t -> {
            int realSize = t.getInteger(size, keys);
            Dimension dim = new Dimension(realSize, realSize);
            this.setPreferredSize(dim);
            this.setMaximumSize(dim);
        });
    }
}
