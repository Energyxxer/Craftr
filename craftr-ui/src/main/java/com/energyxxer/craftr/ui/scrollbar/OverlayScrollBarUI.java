package com.energyxxer.craftr.ui.scrollbar;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by User on 12/13/2016.
 */
public class OverlayScrollBarUI extends BasicScrollBarUI {

    private int thumbSize = 10;
    private Color thumbColor;
    private Color thumbRolloverColor;

    private JScrollPane sp;

    public OverlayScrollBarUI(JScrollPane sp, int size) {
        super();
        this.sp = sp;
        this.thumbSize = size;
        ThemeChangeListener.addThemeChangeListener(t -> {
            thumbColor = t.getColor(new Color(0,0,0,50), "General.scrollbar.color");
            thumbRolloverColor = t.getColor(new Color(0,0,0,100), "General.scrollbar.hover.color");
        });
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // your code
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Color color = isThumbRollover() ? thumbRolloverColor : thumbColor;
        int orientation = scrollbar.getOrientation();
        int x = thumbBounds.x;
        int y = thumbBounds.y;

        int width = orientation == JScrollBar.VERTICAL ? thumbSize : thumbBounds.width;
        //width = Math.max(width, thumbSize);

        int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : thumbSize;
        //height = Math.max(height, thumbSize);

        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.setColor(color);
        graphics2D.fillRect(x, y, width, height);
        graphics2D.dispose();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x,y,width,height);
        sp.repaint();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton jbutton = new JButton();
        jbutton.setOpaque(false);
        jbutton.setFocusable(false);
        jbutton.setFocusPainted(false);
        jbutton.setBorderPainted(false);
        jbutton.setBorder(BorderFactory.createEmptyBorder());
        return jbutton;
    }
}
