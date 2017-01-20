package com.energyxxer.cbe.ui.editor.behavior.caret;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.util.StringLocation;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by User on 1/3/2017.
 */
public class EditorCaret extends DefaultCaret {

    private ArrayList<Dot> dots = new ArrayList<>();
    private AdvancedEditor textComponent;

    private static Highlighter.HighlightPainter nullHighlighter = (g,p0,p1,bounds,c) -> {};

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
                handleEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        dots.add(new Dot(0,textComponent));

        try {
            c.getHighlighter().addHighlight(0,0,new EditorSelectionPainter(this));
        } catch(BadLocationException e) {
            //
        }
    }

    private void handleEvent(KeyEvent e) {
        boolean actionPerformed = false;
        for(Dot dot : dots) {
            if(dot.handleEvent(e)) actionPerformed = true;
        }
        if(actionPerformed) update();
    }

    private void update() {
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
            Dot newDot = new Dot(dot,textComponent);
            if(!dots.contains(newDot)) dots.add(newDot);
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
            StringLocation loc2 = textComponent.getLocationForOffset(dots.get(0).mark);
            return loc.line + ":" + loc.column + " - " + loc2.line + ":" + loc2.column;
        }
    }

    public void moveBy(int offset) {
        pushFrom(0, offset);
    }

    public void pushFrom(int pos, int offset) {
        //System.out.println("Pushing " + offset + " from " + pos);
        int docLength = textComponent.getDocument().getLength();

        for(Dot dot : dots) {
            if(dot.index >= pos) {
                dot.index = Math.min(docLength, Math.max(0, dot.index + offset));
            }
            if(dot.mark >= pos) {
                dot.mark = Math.min(docLength, Math.max(0, dot.mark + offset));
            }
        }

        readjustRect();
        repaint();
        this.fireStateChanged();
    }

    public void deselect() {
        for(Dot dot : dots) {
            dot.deselect();
        }
    }

    @Override
    public int getDot() {
        return dots.get(dots.size()-1).index;
    }

    public ArrayList<Dot> getDots() {
        return new ArrayList<>(this.dots);
    }

    public CaretProfile getProfile() {
        CaretProfile profile = new CaretProfile();
        profile.addAllDots(dots);
        profile.sort();
        return profile;
    }

    @Override
    protected Highlighter.HighlightPainter getSelectionPainter() {
        return nullHighlighter;
    }

    public void adopt(CaretProfile profile) {
        this.dots.clear();
        for(int i = 0; i < profile.size(); i += 2) {
            dots.add(new Dot(profile.get(i),profile.get(i+1), textComponent));
        }
        update();
    }
}
