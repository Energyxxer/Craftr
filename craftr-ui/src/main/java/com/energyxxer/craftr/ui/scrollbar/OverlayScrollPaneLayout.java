package com.energyxxer.craftr.ui.scrollbar;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/13/2016.
 */
public class OverlayScrollPaneLayout extends ScrollPaneLayout {

    private int thumbSize = 10;

    public OverlayScrollPaneLayout() {
        ThemeChangeListener.addThemeChangeListener(t -> {
            thumbSize = t.getInteger(10, "General.scrollbar.thickness");
        });
    }

    @Override
    public void layoutContainer(Container parent) {

        super.layoutContainer(parent);

        Rectangle availR = parent.getBounds();
        if(this.rowHead != null) this.rowHead.setSize(this.rowHead.getWidth(),availR.height);
        availR.x = availR.y = 0;

        // viewport
        Insets insets = parent.getInsets();
        availR.x = insets.left + ((rowHead != null) ? rowHead.getWidth() : 0);
        availR.y = insets.top;
        availR.width -= insets.left + insets.right + ((rowHead != null) ? rowHead.getWidth() : 0);
        availR.height -= insets.top + insets.bottom;
        if (viewport != null) {
            viewport.setBounds(availR);
        }

        boolean vsbNeeded = isVerticalScrollBarNecessary();
        boolean hsbNeeded = isHorizontalScrollBarNecessary();

        if (vsb != null) {
            // vertical scroll bar
            Rectangle vsbR = new Rectangle();
            vsbR.width = thumbSize;
            vsbR.height = availR.height - (hsbNeeded ? vsbR.width : 0);
            vsbR.x = availR.x + availR.width - vsbR.width;
            vsbR.y = availR.y;
            vsb.setBounds(vsbR);
        }

        if (hsb != null) {
            // horizontal scroll bar
            Rectangle hsbR = new Rectangle();
            hsbR.height = thumbSize;
            hsbR.width = availR.width - (vsbNeeded ? hsbR.height : 0);
            hsbR.x = availR.x;
            hsbR.y = availR.y + availR.height - hsbR.height;
            hsb.setBounds(hsbR);
        }
    }

    private boolean isVerticalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getHeight() > viewRect.getHeight();
    }

    private boolean isHorizontalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getWidth() > viewRect.getWidth();
    }
}
