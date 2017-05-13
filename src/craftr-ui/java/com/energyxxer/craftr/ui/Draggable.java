package com.energyxxer.craftr.ui;


import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by User on 12/16/2016.
 */
public class Draggable implements MouseMotionListener {

    private Point offset = new Point();
    private Point point = new Point();

    private JComponent component;
    private int axis;

    private ArrayList<DragListener> dragListeners = new ArrayList<>();

    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;
    public static final int AXIS_BOTH = 3;

    public Draggable(JComponent component) {
        this(component, AXIS_BOTH);
    }

    public Draggable(JComponent component, int axis) {
        this.component = component;
        this.axis = axis;
        this.component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Draggable.this.mousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                Draggable.this.mouseReleased(e);
            }
        });
        this.component.addMouseMotionListener(this);
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    public int getAxis() {
        return axis;
    }

    public void setAxis(int axis) {
        this.axis = axis;
    }

    public boolean isAxis(int axis) {
        return axis == this.axis || this.axis == AXIS_BOTH;
    }

    public Point getOffset() {
        return offset;
    }

    public void addDragListener(@NotNull DragListener l) {
        dragListeners.add(l);
    }



    public void mousePressed(MouseEvent e) {
        this.point = e.getPoint();
        this.offset = new Point();
    }

    public void mouseReleased(MouseEvent e) {
        this.point = e.getPoint();
        dispatchDropEvent(point);
        this.offset = new Point();
    }

    private void dispatchDragEvent(Point offset) {
        for(DragListener l : dragListeners) {
            l.onDrag(offset);
        }
    }

    private void dispatchDropEvent(Point point) {
        for(DragListener l : dragListeners) {
            l.onDrop(point);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        Point tickOffset = new Point(p.x - point.x, p.y - point.y);
        if(!isAxis(AXIS_X)) tickOffset.x = 0;
        if(!isAxis(AXIS_Y)) tickOffset.y = 0;
        offset.translate(tickOffset.x, tickOffset.y);
        dispatchDragEvent(tickOffset);
        this.point = e.getPoint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
