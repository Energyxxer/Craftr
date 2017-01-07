package com.energyxxer.cbe.ui.editor.behavior.caret;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.Rectangle;

/**
 * Created by User on 1/7/2017.
 */
public class Dot {
    JTextComponent component;
    int index = 0;
    int x = 0;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    public Dot(int index, JTextComponent component) {
        this.component = component;
        this.index = index;
        updateX();
    }

    public void move(int dir) {
        switch(dir) {
            case LEFT: {
                index = Math.max(0, Math.min(component.getDocument().getLength(), index-1));
                updateX();
                break;
            }
            case RIGHT: {
                index = Math.max(0, Math.min(component.getDocument().getLength(), index+1));
                updateX();
                break;
            }
            case UP: {
                try {
                    index = Utilities.getPositionAbove(component, index, x);
                } catch(BadLocationException ble) {}
                break;
            }
            case DOWN: {
                try {
                    index = Utilities.getPositionBelow(component, index, x);
                } catch(BadLocationException ble) {}
                break;
            }
        }
    }

    void updateX() {
        try {
            Rectangle view = component.modelToView(index);
            if(view != null) this.x = view.x;
        } catch (BadLocationException ble) {
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dot dot = (Dot) o;

        return index == dot.index;
    }
}
