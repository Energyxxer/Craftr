package com.energyxxer.cbe.ui.editor.behavior.caret;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.util.StringLocation;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 1/3/2017.
 */
public class EditorCaret extends DefaultCaret {

    private ArrayList<Dot> dots = new ArrayList<>();
    private AdvancedEditor textComponent;

    public EditorCaret(AdvancedEditor c) {
        this.textComponent = c;
        this.setBlinkRate(500);
        this.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!e.isAltDown() || !e.isShiftDown()) {
                    dots.clear();
                }
                addDot(c.viewToModel(e.getPoint()));
                e.consume();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.isShiftDown()) {
                    e.consume();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if(e.isShiftDown()) {
                    e.consume();
                }
            }
        };
        c.addMouseListener(mouseAdapter);
        c.addMouseMotionListener(mouseAdapter);
        c.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_LEFT) {
                    e.consume();
                    move(Dot.LEFT);
                } else if(key == KeyEvent.VK_RIGHT) {
                    e.consume();
                    move(Dot.RIGHT);
                } else if(key == KeyEvent.VK_UP) {
                    e.consume();
                    move(Dot.UP);
                } else if(key == KeyEvent.VK_DOWN) {
                    e.consume();
                    move(Dot.DOWN);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        dots.add(new Dot(0,textComponent));
    }

    private void move(int dir) {
        for(Dot dot : dots) {
            dot.move(dir);
        }
        removeDuplicates();
        textComponent.repaint();
        this.setVisible(true);
        readjustRect();
        this.fireStateChanged();
    }

    public void setPosition(int pos) {
        dots.clear();
        dots.add(new Dot(pos, textComponent));
        readjustRect();
        repaint();
        this.fireStateChanged();
    }

    public void addDot(int... pos) {
        for(int dot : pos) {
            if(!dots.contains(dot)) dots.add(new Dot(dot, textComponent));
        }
        readjustRect();
        textComponent.repaint();
        this.fireStateChanged();
    }

    public void removeDuplicates() {
        boolean stateChanged = false;
        for(int i = 0; i < dots.size(); i++) {
            Dot d = dots.get(i);
            if(dots.indexOf(d) != i) {
                dots.remove(i);
                i--;
                stateChanged = true;
            }
        }
        if(stateChanged) this.fireStateChanged();
    }

    @Override
    public void paint(Graphics g) {
        try {
            TextUI mapper = getComponent().getUI();

            ArrayList<Dot> allDots = new ArrayList<>(dots);

            for (Dot dot : allDots) {
                Rectangle r = mapper.modelToView(getComponent(), dot.index, getDotBias());

                if(isVisible()) {
                    g.setColor(getComponent().getCaretColor());
                    int paintWidth = 2;
                    r.x -= paintWidth >> 1;
                    g.fillRect(r.x, r.y, paintWidth, r.height);
                }
                else {
                    getComponent().repaint(r);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void readjustRect() {
        try {
            TextUI mapper = getComponent().getUI();

            ArrayList<Dot> allDots = new ArrayList<>(dots);

            Rectangle unionRect = null;

            for (Dot dot : allDots) {
                Rectangle r = mapper.modelToView(getComponent(), dot.index, getDotBias());
                if(unionRect == null) unionRect = r; else unionRect = unionRect.union(r);
            }

            if(unionRect != null) {
                x = unionRect.x - 1;
                y = unionRect.y;
                width = unionRect.width + 3;
                height = unionRect.height;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void damage(Rectangle rect) {
        readjustRect();
        repaint();
    }

    public String getCaretInfo() {
        if(dots.size() > 1) {
            return dots.size() + " carets";
        } else {
            StringLocation loc = textComponent.getLocationForOffset(dots.get(0).index);
            return loc.line + ":" + loc.column;
        }
    }

    public void moveBy(int offset) {
        pushFrom(0, offset);
    }

    public void pushFrom(int pos, int offset) {
        //System.out.println("Pushing " + offset + " from " + pos);
        int docLength = textComponent.getDocument().getLength();

        for(Dot dot : dots) {
            if(dot.index >= pos) dot.index = Math.min(docLength, Math.max(0, dot.index + offset));
        }

        readjustRect();
        repaint();
        this.fireStateChanged();
    }

    public List<Integer> getFlatLocations() {
        ArrayList<Integer> locations = new ArrayList<>();
        for(Dot dot : dots) {
            locations.add(dot.index);
            locations.add(dot.index);
        }
        locations.sort(Comparator.comparingInt(Integer::intValue));
        //System.out.println(locations);
        return locations;
    }
}
