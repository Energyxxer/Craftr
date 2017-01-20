package com.energyxxer.cbe.ui.editor.behavior.editmanager.edits;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.cbe.ui.editor.behavior.caret.EditorCaret;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.Edit;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;

/**
 * Created by User on 1/10/2017.
 */
public class InsertionEdit implements Edit {
    private String value;
    private ArrayList<String> previousValues = new ArrayList<>();
    private CaretProfile previousProfile = new CaretProfile();
    private CaretProfile nextProfile = null;

    public InsertionEdit(String value, AdvancedEditor editor) {
        this.value = value;
        previousProfile = editor.getCaret().getProfile();
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
                previousValues.add(result.substring(start, end));
                result = result.substring(0, start) + value + result.substring(end);

                nextProfile.add(start+1,start+1);

                ((AbstractDocument) doc).replace(start, end - start, value, null);

                characterDrift += value.length() - (end - start);
            }

            caret.adopt(nextProfile);

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

            for (int i = 0; i < previousProfile.size() - 1; i += 2) {
                int start = previousProfile.get(i);
                int resultEnd = start + value.length();
                if(resultEnd < start) {
                    int temp = start;
                    start = resultEnd;
                    resultEnd = temp;
                }

                String previousValue = previousValues.get(i / 2);

                str = str.substring(0, start) + previousValue + str.substring(resultEnd);

                ((AbstractDocument) doc).replace(start, resultEnd - start, previousValue, null);
            }

            caret.adopt(previousProfile);

        } catch(BadLocationException e) {
            e.printStackTrace();
        }
    }
}
