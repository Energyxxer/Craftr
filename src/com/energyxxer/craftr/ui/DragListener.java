package com.energyxxer.craftr.ui;

import java.awt.Point;

/**
 * Created by User on 12/16/2016.
 */
public interface DragListener {
    void onDrag(Point offset);
    void onDrop(Point point);
}
