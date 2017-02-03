package com.energyxxer.craftr.ui.editor.behavior.editmanager;

import com.energyxxer.craftr.ui.editor.behavior.AdvancedEditor;

/**
 * Created by User on 1/10/2017.
 */
public interface Edit {
    boolean redo(AdvancedEditor editor);
    boolean undo(AdvancedEditor editor);
}
