package com.energyxxer.cbe.ui.editor.behavior.editmanager.edits;

import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.cbe.ui.editor.behavior.caret.Dot;
import com.energyxxer.cbe.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.Edit;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;

/**
 * Created by User on 1/10/2017.
 */
public class DeletionEdit implements Edit {
    private boolean wholeWord = false;
    private ArrayList<String> previousValues = new ArrayList<>();
    private CaretProfile previousProfile = new CaretProfile();
    private CaretProfile nextProfile = null;

    public DeletionEdit(AdvancedEditor editor) {
        this(editor, false);
    }
    public DeletionEdit(AdvancedEditor editor, boolean wholeWord) {
        previousProfile = editor.getCaret().getProfile();
        this.wholeWord = wholeWord;
    }

    @Override
    public void redo(AdvancedEditor editor) {
        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String result = doc.getText(0, doc.getLength()); //Result

            int characterDrift = 0;

            previousValues.clear();
            nextProfile = new CaretProfile();

            for (int i = 0; i < previousProfile.size() - 1; i += 2) {
                int start = previousProfile.get(i) + characterDrift;
                int end = previousProfile.get(i + 1) + characterDrift;
                if(end < start) {
                    int temp = start;
                    start = end;
                    end = temp;
                }
                if(start == end) {
                    if(wholeWord) {
                        start = new Dot(start, end, editor).getPositionBeforeWord();
                    } else {
                        start = Math.max(start-1,0);
                    }
                }

                previousValues.add(result.substring(start, end));
                result = result.substring(0, start) + result.substring(end);

                nextProfile.add(start,start);
                doc.remove(start, end - start);

                characterDrift += start - end;
            }

            caret.setProfile(nextProfile);

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo(AdvancedEditor editor) {
        Document doc = editor.getDocument();
        EditorCaret caret = editor.getCaret();
        try {
            String str = doc.getText(0, doc.getLength());

            Window.statusBar.setStatus(previousProfile.toString());

            for (int i = 0; i < previousProfile.size() - 1; i += 2) {
                int start = nextProfile.get(i);
                //if(start == previousProfile.get(i+1)) start--;
                String previousValue = previousValues.get(i / 2);

                str = str.substring(0, start)
                        + previousValue
                        + str.substring(start);

                doc.insertString(start, previousValue, null);
            }

            caret.setProfile(previousProfile);

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
    }
}
