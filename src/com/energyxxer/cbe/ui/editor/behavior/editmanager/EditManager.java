package com.energyxxer.cbe.ui.editor.behavior.editmanager;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;

import java.util.ArrayList;

/**
 * Created by User on 1/5/2017.
 */
public class EditManager {
    private ArrayList<Edit> edits = new ArrayList<>();
    private int currentEdit = 0;

    private AdvancedEditor component;

    public EditManager(AdvancedEditor component) {
        this.component = component;
    }

    public void undo() {
        if(currentEdit-1 >= 0) edits.get(--currentEdit).undo(component);
    }

    public void redo() {
        if(currentEdit < edits.size()) edits.get(currentEdit++).redo(component);
    }

    public void insertEdit(Edit edit) {
        while(edits.size() > currentEdit) {
            edits.remove(currentEdit);
        }
        edits.add(edit);
        redo();
    }
}
