package com.energyxxer.cbe.ui.editor;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.inspector.Inspector;

import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Date;
import java.util.Set;

/**
 * Created by User on 1/1/2017.
 */
public class CBEEditorComponent extends AdvancedEditor implements KeyListener, CaretListener, ActionListener {

    private CBEEditor parent;

    private StyledDocument sd;

    Inspector inspector;

    private long lastEdit;


    public CBEEditorComponent(CBEEditor parent) {
        super(new DefaultStyledDocument());
        this.parent = parent;

        sd = this.getStyledDocument();

        this.inspector = new Inspector(parent.associatedTab, this);

        this.addCaretListener(this);

        Timer timer = new Timer(20, this);
        timer.start();

        //this.setOpaque(false);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        displayCaretInfo();
    }

    private void highlightSyntax() throws BadLocationException {

        sd.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        sd.setCharacterAttributes(0, sd.getLength(), defaultStyle, true);

        String text = getText();

        new Analyzer(new File(parent.associatedTab.path), text, new TokenStream(true) {
            @Override
            public void onWrite(Token token) {
                Style style = CBEEditorComponent.this.getStyle(token.type.toLowerCase());
                if(style != null)
                    sd.setCharacterAttributes(token.loc.index, token.value.length(), style, true);

                Set<String> set = token.attributes.keySet();
                for(String key : set) {
                    if(!token.attributes.get(key).equals(true)) continue;
                    Style attrStyle = CBEEditorComponent.this.getStyle("#" + key.toLowerCase());
                    if(attrStyle == null) continue;
                    sd.setCharacterAttributes(token.loc.index, token.value.length(), attrStyle, false);
                }
            }
        });
    }

    void highlight() {
        lastEdit = new Date().getTime();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (lastEdit > -1 && (new Date().getTime()) - lastEdit > 500 && parent.associatedTab.isActive()) {
            new Thread(() -> {
                try {
                    highlightSyntax();
                } catch(BadLocationException e) {
                    e.printStackTrace();
                }
                this.inspector.inspect();
            },"Text Highlighter").start();
            lastEdit = -1;
        }
    }

    public void displayCaretInfo() {
        Window.statusBar.setCaretInfo(getCaretInfo());
    }
}
