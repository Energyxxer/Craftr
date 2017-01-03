package com.energyxxer.cbe.ui.editor;

import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.util.StringLocation;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Utilities;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by User on 1/1/2017.
 */
public class EditorComponent extends JTextPane implements KeyListener, CaretListener {

    private Editor parent;

    private StringLocation caretLocation = new StringLocation(0,1,1);

    public EditorComponent(Editor parent) {
        super(new DefaultStyledDocument());
        this.parent = parent;

        this.addKeyListener(this);
        this.addCaretListener(this);

        //this.setOpaque(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if(c == '`') {
            e.consume();
            this.setCaretPosition(0);
        }
        /*try {
            if(c == 'a') {
                System.out.println("Consuming");
                e.consume();
                if(false) {
                    this.getDocument().insertString(this.getCaretPosition(),"    ",null);
                }
            }
        } catch(BadLocationException ble) {
            ble.printStackTrace();
        }*/
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume();
            try {
                if(this.getSelectedText() == null) {
                    String str = "";
                    int spaces = 4 - ((caretLocation.column-1) % 4);
                    spaces = (spaces > 0) ? spaces : 4;
                    for(int i = 0; i < spaces; i++) str += " ";
                    this.getDocument().insertString(this.getCaretPosition(),str,null);
                }
            } catch(BadLocationException ble) {
                ble.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {
        try {
            int index = e.getDot();
            int line = (index == 0) ? 1 : 0;
            try {
                int offset = index;
                while(offset > 0) {
                    int rs = Utilities.getRowStart(this, offset);
                    if(rs < 0) {
                        line = 1;
                        break;
                    }
                    offset = rs - 1;
                    line++;
                }
            } catch(BadLocationException ble) {
                ble.printStackTrace();
            }
            int column;
            int rs = Utilities.getRowStart(this, index);
            column = (rs >= 0) ? index - rs + 1 : 1;
            this.caretLocation = new StringLocation(index, line, column);
            displayCaretInfo();
        } catch(BadLocationException ble) {
            ble.printStackTrace();
        }
    }

    public void displayCaretInfo() {
        Window.statusBar.setCaretInfo(caretLocation.line + ":" + caretLocation.column);
    }
}
