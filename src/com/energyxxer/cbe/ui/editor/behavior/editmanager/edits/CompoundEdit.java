package com.energyxxer.cbe.ui.editor.behavior.editmanager.edits;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.behavior.editmanager.Edit;

import java.util.ArrayList;

/**
 * Created by User on 1/6/2017.
 */
public class CompoundEdit implements Edit {
    private ArrayList<Edit> edits = new ArrayList<>();

    public CompoundEdit() {
    }

    public CompoundEdit(ArrayList<Edit> edits) {
        this.edits = edits;
    }

    public void appendEdit(Edit edit) {
        edits.add(edit);
    }

    @Override
    public void redo(AdvancedEditor editor) {
        for(Edit e : edits) {
            e.redo(editor);
        }
    }

    @Override
    public void undo(AdvancedEditor editor) {
        for(int i = edits.size()-1; i >= 0; i--) {
            Edit e = edits.get(i);
            e.undo(editor);
        }
    }
}
