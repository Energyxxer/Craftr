package com.energyxxer.cbe.ui.editor.behavior.editmanager;

import com.energyxxer.cbe.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.cbe.ui.editor.behavior.caret.CaretProfile;

import java.util.ArrayList;

/**
 * Created by User on 1/5/2017.
 */
public class EditManager {
    private ArrayList<Edit> edits = new ArrayList<>();
    private int currentEdit = 0;
    private CaretProfile lastProfile = null;

    private AdvancedEditor component;

    public EditManager(AdvancedEditor component) {
        this.component = component;
    }

    public void undo() {
        if(currentEdit-1 >= 0) {
            if(component.getCaret().getProfile().equals(lastProfile)) {
                edits.get(--currentEdit).undo(component);
                lastProfile = component.getCaret().getProfile();
            } else {
                component.getCaret().setProfile(lastProfile);
            }
        }
    }

    public void redo() {
        if(currentEdit < edits.size()) {
            if(component.getCaret().getProfile().equals(lastProfile)) {
                edits.get(currentEdit++).redo(component);
                lastProfile = component.getCaret().getProfile();
            } else {
                component.getCaret().setProfile(lastProfile);
            }
        }
    }

    public void insertEdit(Edit edit) {
        if(edit.redo(component)) {
            while(edits.size() > currentEdit) {
                edits.remove(currentEdit);
            }
            edits.add(edit);
            currentEdit++;
            lastProfile = component.getCaret().getProfile();
        }
    }
}
