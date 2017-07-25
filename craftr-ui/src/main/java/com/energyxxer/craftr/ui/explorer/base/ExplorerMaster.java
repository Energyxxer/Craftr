package com.energyxxer.craftr.ui.explorer.base;

import com.energyxxer.craftr.ui.explorer.base.elements.ExplorerElement;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 2/7/2017.
 */
public class ExplorerMaster extends JPanel implements MouseListener, MouseMotionListener {
    protected HashMap<ExplorerFlag, Boolean> explorerFlags = new HashMap<>();

    protected ArrayList<ExplorerElement> children = new ArrayList<>();
    protected ArrayList<ExplorerElement> selectedItems = new ArrayList<>();

    protected ExplorerElement rolloverItem = null;

    private ArrayList<String> expandedElements = new ArrayList<>();

    protected ArrayList<ExplorerElement> flatList = new ArrayList<>();
    private int contentWidth = 0;
    private int offsetY = 0;

    protected HashMap<String, Color> colors = new HashMap<>();
    protected HashMap<String, Image> assets = new HashMap<>();

    protected int rowHeight = 20;
    protected int indentPerLevel = 20;
    protected int initialIndent = 0;
    protected String selectionStyle = "FULL";
    protected int selectionLineThickness = 2;

    public ExplorerMaster() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        explorerFlags.put(ExplorerFlag.FLATTEN_EMPTY_PACKAGES, true);
        explorerFlags.put(ExplorerFlag.SHOW_PROJECT_FILES, true);
        explorerFlags.put(ExplorerFlag.DYNAMIC_ROW_HEIGHT, false);
    }

    public void refresh() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        offsetY = 0;
        flatList.clear();
        g.setColor(colors.get("background"));
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        for(ExplorerElement i : children) {
            i.render(g);
        }

        if(this.getPreferredSize().height != offsetY + rowHeight) {
            this.setPreferredSize(new Dimension(contentWidth, offsetY + rowHeight));
            this.setSize(new Dimension(contentWidth, offsetY + rowHeight));
            this.getParent().revalidate();
        }
    }

    @Override
    public void repaint() {
        if(this.getParent() instanceof JViewport && this.getParent().getParent() instanceof JScrollPane) {
            this.getParent().getParent().repaint();
        } else super.repaint();
    }

    private ExplorerElement getElementAtMousePos(MouseEvent e) {
        if(getFlag(ExplorerFlag.DYNAMIC_ROW_HEIGHT)) {
            int y = 0;
            for(ExplorerElement element : flatList) {
                if(e.getY() > y && e.getY() < y + element.getHeight()) return element;
                y += element.getHeight();
            }
            return null;
        } else {
            int index = e.getY() / rowHeight;
            if(index >= 0 && index < flatList.size()) {
                return flatList.get(index);
            }
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) element.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) element.mousePressed(e);
        else if(e.getButton() == MouseEvent.BUTTON1) {
            clearSelected();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) element.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) element.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) element.mouseExited(e);

        if(rolloverItem != null) {
            rolloverItem.setRollover(false);
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ExplorerElement element = getElementAtMousePos(e);
        if(element != null) {
            if(rolloverItem != null) rolloverItem.setRollover(false);
            rolloverItem = element;
            rolloverItem.setRollover(true);
        } else {
            if(rolloverItem != null) rolloverItem.setRollover(false);
            rolloverItem = null;
        }
        repaint();
    }

    private void clearSelected() {
        for(ExplorerElement item : selectedItems) {
            item.setSelected(false);
        }
        selectedItems.clear();
    }

    private void addSelected(ExplorerElement item) {
        this.addSelected(item, true);
    }

    private void addSelected(ExplorerElement item, boolean invert) {
        if(!selectedItems.contains(item)) {
            item.setSelected(true);
            selectedItems.add(item);
        } else if(invert) {
            item.setSelected(false);
            selectedItems.remove(item);
        }
    }

    public void setSelected(ExplorerElement item, MouseEvent e) {
        ExplorerElement lastItem = null;
        if(this.selectedItems.size() > 0) lastItem = this.selectedItems.get(this.selectedItems.size()-1);
        if(!e.isControlDown()) {
            clearSelected();
        }
        if(e.isShiftDown() && lastItem != null) {
            int startIndex = flatList.indexOf(lastItem);
            int endIndex = flatList.indexOf(item);

            int start = Math.min(startIndex, endIndex);
            int end = Math.max(startIndex, endIndex);
            for(int i = start; i <= end; i++) {
                addSelected(flatList.get(i), false);
            }
        } else {
            addSelected(item);
        }
        repaint();
    }

    public List<String> getSelectedFiles() {
        List<String> list = new ArrayList<>();
        selectedItems.forEach(item -> {
            String path = item.getIdentifier();
            if(path != null) list.add(path);
        });
        return list;
    }

    public boolean getFlag(ExplorerFlag flag) {
        return this.explorerFlags.get(flag);
    }

    public ArrayList<ExplorerElement> getFlatList() {
        return flatList;
    }

    public HashMap<String, Color> getColors() {
        return colors;
    }

    public HashMap<String, Image> getAssets() {
        return assets;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getIndentPerLevel() {
        return indentPerLevel;
    }

    public void setIndentPerLevel(int indentPerLevel) {
        this.indentPerLevel = indentPerLevel;
    }

    public int getInitialIndent() {
        return initialIndent;
    }

    public void setInitialIndent(int initialIndent) {
        this.initialIndent = initialIndent;
    }

    public String getSelectionStyle() {
        return selectionStyle;
    }

    public void setSelectionStyle(String selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    public int getSelectionLineThickness() {
        return selectionLineThickness;
    }

    public void setSelectionLineThickness(int selectionLineThickness) {
        this.selectionLineThickness = selectionLineThickness;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public ArrayList<String> getExpandedElements() {
        return expandedElements;
    }

    public void setExpandedElements(ArrayList<String> expandedElements) {
        this.expandedElements = expandedElements;
    }

    public ArrayList<ExplorerElement> getChildren() {
        return children;
    }
}