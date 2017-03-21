package com.energyxxer.craftr.ui.editor.behavior.editmanager;

import com.energyxxer.craftr.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.craftr.ui.editor.behavior.caret.CaretProfile;

import java.util.ArrayList;

/**
 * Created by User on 1/5/2017.
 */
public class EditManager {
    private ArrayList<Edit> edits = new ArrayList<>();
    private int currentEdit = 0;
    private CaretProfile lastProfile = null;

    private static final int MERGE_DIFFERENCE = 300;

    private AdvancedEditor component;

    public EditManager(AdvancedEditor component) {
        this.component = component;
    }

    public void undo() {
        if(currentEdit-1 >= 0) {
            if(component.getCaret().getProfile().equals(lastProfile)) {
                edits.get(--currentEdit).undo(component);
                lastProfile = component.getCaret().getProfile();
                if(currentEdit > 0 && Math.abs(edits.get(currentEdit).time - edits.get(currentEdit-1).time) <= MERGE_DIFFERENCE) undo();
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
                if(currentEdit < edits.size() &&
                   Math.abs(edits.get(currentEdit-1).time - edits.get(currentEdit).time) <= MERGE_DIFFERENCE) {
                    redo();
                }
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
