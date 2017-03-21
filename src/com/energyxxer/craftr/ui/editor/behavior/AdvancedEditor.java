package com.energyxxer.craftr.ui.editor.behavior;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.craftr.ui.editor.behavior.caret.Dot;
import com.energyxxer.craftr.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.EditManager;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.DeletionEdit;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.IndentEdit;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.InsertionEdit;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.LineMoveEdit;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.NewlineEdit;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.edits.TabInsertionEdit;
import com.energyxxer.craftr.util.StringLocation;
import com.energyxxer.craftr.util.linepainter.LinePainter;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Created by User on 1/5/2017.
 */
public class AdvancedEditor extends JTextPane implements KeyListener, CaretListener {

    private EditorCaret caret;

    private EditManager editManager = new EditManager(this);
    private LinePainter linePainter;

    private HashMap<Integer, Integer> lineLocations = new HashMap<>();
    //               (line)  (index)

    private static final float BIAS_POINT = 0.4f;

    {
        this.addKeyListener(this);
        this.addCaretListener(this);

        linePainter = new LinePainter(this);
        this.setCaret(this.caret = new EditorCaret(this));

        //this.getInputMap().setParent(null);
        this.setInputMap(JComponent.WHEN_FOCUSED,new InputMap());

        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK),"undo");
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK),"redo");

        this.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editManager.undo();
            }
        });
        this.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editManager.redo();
            }
        });

        this.getDocument().addUndoableEditListener(e -> {
            lineLocations.clear();
            lineLocations.put(0,0);
        });

    }

    public AdvancedEditor() {
    }

    public AdvancedEditor(StyledDocument doc) {
        super(doc);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
        CraftrWindow.setStatus(e.getKeyChar() + ":" + Character.getName(e.getKeyChar()));
        /*if(e.getKeyChar() == '`') {
            try {
                Object rawContents = this.getToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if(rawContents == null) return;
                String contents = ((String) rawContents);
                String log = "";
                for(byte c : contents.getBytes()) {
                    log += "" + c + ": \"" + (char) c + "\" (" + Character.getName(c) + ")\n";
                }
                Console.debug.println(log);
            } catch(Exception x) {
                x.printStackTrace();
            }
        } else */if(!e.isControlDown() && !Commons.isSpecialCharacter(e.getKeyChar())) {
            editManager.insertEdit(new InsertionEdit("" + e.getKeyChar(), this));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_TAB) {
            e.consume();

            CaretProfile profile = caret.getProfile();
            if(profile.getSelectedCharCount() == 0) {
                editManager.insertEdit(new TabInsertionEdit(this));
            } else {
                editManager.insertEdit(new IndentEdit(this, e.isShiftDown()));
            }
        } else if(keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_DELETE) {
            e.consume();
            editManager.insertEdit(new DeletionEdit(this, e.isControlDown(), keyCode == KeyEvent.VK_DELETE));
        } else if(keyCode == KeyEvent.VK_ENTER) {
            e.consume();
            editManager.insertEdit(new NewlineEdit(this, !e.isControlDown()));
        } else if(keyCode == KeyEvent.VK_V && e.isControlDown()) {
            e.consume();
            try {
                Object rawContents = this.getToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if(rawContents == null) return;
                String contents = ((String) rawContents).replace("\t", "    ");
                editManager.insertEdit(new InsertionEdit(contents, this));
            } catch(Exception x) {
                x.printStackTrace();
            }
        } else if(keyCode == KeyEvent.VK_A && e.isControlDown()) {
            e.consume();
            caret.setProfile(new CaretProfile(0, getDocument().getLength()));
        } else if(keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN && e.isAltDown()) {
            e.consume();
            if(keyCode == KeyEvent.VK_UP) {
                editManager.insertEdit(new LineMoveEdit(this, Dot.UP));
            }
            else if(keyCode == KeyEvent.VK_DOWN) {
                editManager.insertEdit(new LineMoveEdit(this, Dot.DOWN));
            }
        } else if(keyCode == KeyEvent.VK_ESCAPE) {
            int dotPos = caret.getDot();
            caret.setProfile(new CaretProfile(dotPos, dotPos));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void caretUpdate(CaretEvent e) {

    }

    @Override
    public int viewToModel(Point pt) {
        int superResult = super.viewToModel(pt);
        try {
            char ch = this.getDocument().getText(superResult,1).charAt(0);
            if(ch == '\n') return superResult;
            Rectangle backward = this.modelToView(superResult);
            Rectangle forward = this.modelToView(superResult+1);

            float offset = (float) (pt.x - backward.x) / (forward.x - backward.x);
            if(offset < 0) {
                return (1+offset >= BIAS_POINT) ? superResult : Math.max(superResult-1,0);
            }
            return (offset >= BIAS_POINT) ? superResult+1 : superResult;
        } catch(BadLocationException x) {
            x.printStackTrace();
            return superResult;
        }
    }

    public StringLocation getLocationForOffset(int index) {
        int line = 0;
        int lineStart = 0;
        for(int l = 0; l < lineLocations.size(); l++) {
            if(lineLocations.get(l) < index) {
                line = l;
                lineStart = lineLocations.get(l);
            } else if(lineLocations.get(l) == index) {
                return new StringLocation(index, l+1, 1);
            } else {
                break;
            }
        }
        Document doc = this.getDocument();
        try {
            String str = doc.getText(lineStart, doc.getLength() - lineStart);

            int column = 0;

            for(int i = 0; i < str.length(); i++) {
                if(lineStart + i == index) {
                    return new StringLocation(index, line + 1, column + 1);
                }
                if(str.charAt(i) == '\n') {
                    line++;
                    column = 0;
                    lineLocations.putIfAbsent(line, lineStart + i + 1);
                } else {
                    column++;
                }
            }
            return new StringLocation(index, line + 1, column + 1);
        } catch (BadLocationException x) {
            x.printStackTrace();
            return null;
        }
        /*
        Console.debug.println("getting location for offset " + index);
        try {
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
            return new StringLocation(index, line, column);
        } catch(BadLocationException e) {
            return null;
        }*/
    }

    protected String getCaretInfo() {
        return caret.getCaretInfo();
    }

    protected String getSelectionInfo() {
        return caret.getSelectionInfo();
    }

    @Override
    public EditorCaret getCaret() {
        return caret;
    }

    public void setCurrentLineColor(Color c) {
        linePainter.setColor(c);
    }

    //Delegates and deprecated methods

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getUI().getPreferredSize(this).width + 5
                <= getParent().getSize().width;
    }

    @Override
    public void setCaretPosition(int position) {
        caret.setPosition(position);
    }

    @Override
    public int getCaretPosition() {
        return super.getCaretPosition();
    }

    @Deprecated
    public void replaceSelection(String content) {
        super.replaceSelection(content);
    }

    @Deprecated
    public void moveCaretPosition(int pos) {
        super.moveCaretPosition(pos);
    }

    @Deprecated
    public String getSelectedText() {
        return super.getSelectedText();
    }

    @Deprecated
    public int getSelectionStart() {
        return super.getSelectionStart();
    }

    @Deprecated
    public void setSelectionStart(int selectionStart) {
        super.setSelectionStart(selectionStart);
    }

    @Deprecated
    public int getSelectionEnd() {
        return super.getSelectionEnd();
    }

    @Deprecated
    public void setSelectionEnd(int selectionEnd) {
        super.setSelectionEnd(selectionEnd);
    }

    @Deprecated
    public void select(int selectionStart, int selectionEnd) {
        super.select(selectionStart, selectionEnd);
    }

    @Deprecated
    public void selectAll() {
        super.selectAll();
    }
}
