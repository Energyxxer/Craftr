package com.energyxxer.craftr.ui.components;

import javax.swing.JPanel;
import java.awt.Dimension;

/**
 * Created by User on 2/11/2017.
 */
public class Padding extends JPanel {

    public Padding(int size) {
        this(size, size);
    }

    public Padding(int width, int height) {
        this.setOpaque(false);
        Dimension dim = new Dimension(width, height);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
    }
}
