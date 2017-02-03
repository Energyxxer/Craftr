package com.energyxxer.craftr.ui.editor.behavior.editmanager.edits;

import com.energyxxer.craftr.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.craftr.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.craftr.ui.editor.behavior.caret.Dot;
import com.energyxxer.craftr.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.Edit;
import com.energyxxer.craftr.util.StringUtil;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Created by User on 1/27/2017.
 */
public class IndentEdit implements Edit {
    private CaretProfile previousProfile = new CaretProfile();
    private String previousText;
    private final boolean reverse;

    public IndentEdit(AdvancedEditor editor) {
        this(editor,false);
    }

    public IndentEdit(AdvancedEditor editor, boolean reverse) {
        previousProfile = editor.getCaret().getProfile();
        this.reverse = reverse;
    }

    @Override
    public boolean redo(AdvancedEditor editor) {
        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();

        boolean actionPerformed = false;

        try {
            String text = doc.getText(0, doc.getLength()); //Result
            this.previousText = text;

            int characterDrift = 0;

            CaretProfile nextProfile = new CaretProfile(previousProfile);

            for (int i = 0; i < previousProfile.size()-1; i += 2) {
                //Get bounds of the line to move
                int selectionStart = previousProfile.get(i) + characterDrift;
                int selectionEnd = previousProfile.get(i + 1) + characterDrift;

                int start = new Dot(Math.min(selectionStart,selectionEnd),editor).getRowStart();
                int end = new Dot(Math.max(selectionStart,selectionEnd),editor).getRowEnd();

                for(int l = start; l <= end + characterDrift; l = new Dot(l, editor).getPositionBelow()) {
                    int spaces = StringUtil.getSequenceCount(text," ", l - characterDrift);
                    //Console.debug.println(spaces);
                    if(!reverse) {
                        int spacesToAdd = 4 - (spaces % 4);
                        spacesToAdd = (spacesToAdd > 0) ? spacesToAdd : 4;
                        doc.insertString(l,StringUtil.repeat(" ", spacesToAdd),null);
                        nextProfile.pushFrom(l,spacesToAdd);
                        actionPerformed = true;
                        if(l == end + characterDrift) break;
                        characterDrift += spacesToAdd;
                    } else {
                        if(spaces == 0) {
                            if(l == end + characterDrift) break; continue;
                        }
                        int spacesToRemove = (spaces % 4 == 0) ? 4 : spaces % 4;
                        if(spacesToRemove != 0) {
                            nextProfile.pushFrom(l,-spacesToRemove);
                        }
                        actionPerformed = true;
                        doc.remove(l,spacesToRemove);
                        if(l == end + characterDrift) break;
                        characterDrift -= spacesToRemove;
                    }
                }
            }

            caret.setProfile(nextProfile);
        } catch(BadLocationException x) {
            x.printStackTrace();
        }
        return actionPerformed;
    }

    @Override
    public boolean undo(AdvancedEditor editor) {
        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();

        try {
            //Too complicated, just put back the text from before.

            doc.remove(0,doc.getLength());
            doc.insertString(0, this.previousText, null);
            caret.setProfile(previousProfile);
        } catch(BadLocationException x) {
            x.printStackTrace();
        }
        return true;
    }
}
