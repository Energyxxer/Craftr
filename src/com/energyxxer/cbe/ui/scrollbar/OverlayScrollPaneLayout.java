package com.energyxxer.cbe.ui.scrollbar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/13/2016.
 */
public class OverlayScrollPaneLayout extends ScrollPaneLayout {

    @Override
    public void layoutContainer(Container parent) {

        super.layoutContainer(parent);

        Rectangle availR = parent.getBounds();
        this.rowHead.setSize(this.rowHead.getWidth(),availR.height);
        availR.x = availR.y = 0;


        // viewport
        Insets insets = parent.getInsets();
        availR.x = insets.left + rowHead.getWidth();
        availR.y = insets.top;
        availR.width -= insets.left + insets.right + rowHead.getWidth();
        availR.height -= insets.top + insets.bottom;
        if (viewport != null) {
            viewport.setBounds(availR);
        }

        boolean vsbNeeded = isVerticalScrollBarNecessary();
        boolean hsbNeeded = isHorizontalScrollBarNecessary();

        // vertical scroll bar
        Rectangle vsbR = new Rectangle();
        vsbR.width = 10;
        vsbR.height = availR.height - (hsbNeeded ? vsbR.width : 0);
        vsbR.x = availR.x + availR.width - vsbR.width;
        vsbR.y = availR.y;
        if (vsb != null) {
            vsb.setBounds(vsbR);
        }

        // horizontal scroll bar
        Rectangle hsbR = new Rectangle();
        hsbR.height = 10;
        hsbR.width = availR.width - (vsbNeeded ? hsbR.height : 0);
        hsbR.x = availR.x;
        hsbR.y = availR.y + availR.height - hsbR.height;
        if (hsb != null) {
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
