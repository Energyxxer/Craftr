package com.energyxxer.craftr.ui.explorer;

import java.awt.Graphics;
import java.awt.event.MouseListener;

/**
 * Created by User on 4/8/2017.
 */
public abstract class ExplorerElement implements MouseListener {
    protected boolean selected;
    protected boolean rollover;

    abstract void render(Graphics g);

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isRollover() {
        return rollover;
    }

    public void setRollover(boolean rollover) {
        this.rollover = rollover;
    }

    public abstract String getPath();
}
