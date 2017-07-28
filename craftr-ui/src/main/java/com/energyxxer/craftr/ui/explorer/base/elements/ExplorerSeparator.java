package com.energyxxer.craftr.ui.explorer.base.elements;

import com.energyxxer.craftr.ui.explorer.base.ExplorerMaster;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by User on 4/8/2017.
 */
public class ExplorerSeparator extends ExplorerElement {

    private ExplorerMaster master;

    public ExplorerSeparator(ExplorerMaster master) {
        this.master = master;
    }

    @Override
    public void render(Graphics g) {
        master.getFlatList().add(this);

        g.setColor((this.rollover || this.selected) ? master.getColors().get("item.rollover.background") : master.getColors().get("item.background"));
        g.fillRect(0, master.getOffsetY(), master.getWidth(), this.getHeight());

        g.setColor(master.getColors().get("item.foreground"));
        g.fillRect(master.getWidth() / 10, master.getOffsetY() + ((this.getHeight() / 2) - 1), 8 * (master.getWidth() / 10), 2);

        master.setOffsetY(master.getOffsetY() + getHeight());
    }

    @Override
    public int getHeight() {
        return master.getRowHeight();
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
