package com.energyxxer.craftr.ui.explorer.base.elements;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by User on 4/8/2017.
 */
public abstract class ExplorerElement implements MouseListener {
    protected boolean selected;
    protected boolean rollover;
    protected boolean expanded;

    protected ArrayList<ExplorerElement> children = new ArrayList<>();

    public abstract void render(Graphics g);

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

    public abstract int getHeight();
}
