package com.energyxxer.craftr.ui.explorer;

import java.awt.Graphics;
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
    void render(Graphics g) {
        int y = master.offsetY;
        master.flatList.add(this);

        g.setColor((this.rollover || this.selected) ? ExplorerMaster.colors.get("item.rollover.background") : ExplorerMaster.colors.get("item.background"));
        g.fillRect(0, master.offsetY, master.getWidth(), ExplorerMaster.ROW_HEIGHT);
        if(this.selected) {
            g.setColor(ExplorerMaster.colors.get("item.selected.background"));

            switch(ExplorerMaster.SELECTION_STYLE) {
                case "FULL": {
                    g.fillRect(0, master.offsetY, master.getWidth(), ExplorerMaster.ROW_HEIGHT);
                    break;
                }
                case "LINE_LEFT": {
                    g.fillRect(0, master.offsetY, ExplorerMaster.SELECTION_LINE_THICKNESS, ExplorerMaster.ROW_HEIGHT);
                    break;
                }
                case "LINE_RIGHT": {
                    g.fillRect(master.getWidth() - ExplorerMaster.SELECTION_LINE_THICKNESS, master.offsetY, ExplorerMaster.SELECTION_LINE_THICKNESS, ExplorerMaster.ROW_HEIGHT);
                    break;
                }
                case "LINE_TOP": {
                    g.fillRect(0, master.offsetY, master.getWidth(), ExplorerMaster.SELECTION_LINE_THICKNESS);
                    break;
                }
                case "LINE_BOTTOM": {
                    g.fillRect(0, master.offsetY + ExplorerMaster.ROW_HEIGHT - ExplorerMaster.SELECTION_LINE_THICKNESS, master.getWidth(), ExplorerMaster.SELECTION_LINE_THICKNESS);
                    break;
                }
            }
        }

        master.offsetY += ExplorerMaster.ROW_HEIGHT;
    }

    @Override
    public String getPath() {
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
