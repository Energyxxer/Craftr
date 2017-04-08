package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.components.XIcon;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import java.awt.image.BufferedImage;

/**
 * Created by User on 2/11/2017.
 */
public class StyledIcon extends XIcon {
    public StyledIcon(String icon) {
        this(icon, -1, -1, -1);
    }

    public StyledIcon(String icon, int width, int height, int hints) {
        if(width + height < 0) {
            ThemeChangeListener.addThemeChangeListener(t -> {
                this.setImage((BufferedImage) Commons.getIcon(icon).getScaledInstance(width, height, hints));
            });
        } else {
            ThemeChangeListener.addThemeChangeListener(t -> {
                this.setImage(Commons.getIcon(icon));
            });
        }
    }
}
