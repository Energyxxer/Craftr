package com.energyxxer.cbe.ui.editor.inspector;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.Tab;
import com.energyxxer.cbe.ui.editor.CBEEditorComponent;
import com.energyxxer.cbe.util.StringBounds;

import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by User on 1/1/2017.
 */
public class Inspector implements Highlighter.HighlightPainter {

    volatile protected ArrayList<InspectionItem> items = new ArrayList<>();

    protected Tab tab;

    public Inspector(Tab tab, CBEEditorComponent editor) {
        this.tab = tab;

        try
        {
            editor.getHighlighter().addHighlight(0, 0, this);
        }
        catch(BadLocationException ble) {}
    }

    public void inspect() {
        items.clear();
        TokenStream ts = new TokenStream();
        new Analyzer(new File(tab.path), tab.editor.getText(), ts);

        for(InspectionStructureMatch inspect : InspectionStructures.getAll()) {
            ArrayList<TokenPattern<?>> matches = ts.search(inspect);
            for(TokenPattern<?> match : matches) {
                items.add(new InspectionItem(match, inspect.type));
            }
        }
        tab.editor.editorComponent.repaint();
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape graphicBounds, JTextComponent c) {
        try {
            for (InspectionItem item : items) {

                g.setColor(Window.getTheme().getColor(item.type.colorKey));

                try {

                    StringBounds bounds = item.pattern.getStringBounds();

                    for (int l = bounds.start.line; l <= bounds.end.line; l++) {
                        Rectangle rectangle;
                        if (l == bounds.start.line) {
                            rectangle = tab.editor.editorComponent.modelToView(bounds.start.index);
                            if (bounds.start.line == bounds.end.line) {
                                rectangle.width = tab.editor.editorComponent.modelToView(bounds.end.index).x - rectangle.x;
                            } else {
                                rectangle.width = c.getWidth() - rectangle.x;
                            }
                        } else if (l == bounds.end.line) {
                            rectangle = tab.editor.editorComponent.modelToView(bounds.end.index);
                            rectangle.width = rectangle.x;
                            rectangle.x = 0;
                        } else {
                            rectangle = tab.editor.editorComponent.modelToView(bounds.start.index);
                            rectangle.x = 0;
                            rectangle.y += rectangle.height * (l - bounds.start.line);
                            rectangle.width = c.getWidth();
                        }

                        if (item.type.line) {
                            for (int x = rectangle.x; x < rectangle.x + rectangle.width; x += 4) {
                                g.drawLine(x, rectangle.y + rectangle.height, x + 2, rectangle.y + rectangle.height - 2);
                                g.drawLine(x + 2, rectangle.y + rectangle.height - 2, x + 4, rectangle.y + rectangle.height);
                            }
                        } else {
                            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
                        }
                    }
                } catch (BadLocationException e) {
                    //e.printStackTrace();
                }
            }
        } catch(ConcurrentModificationException e) {}
    }

    public void drawInspections(Graphics g) {


        //fields.get(0).getStringLocation();

        //g.drawString(ts.toString(),0,200);
    }
}
