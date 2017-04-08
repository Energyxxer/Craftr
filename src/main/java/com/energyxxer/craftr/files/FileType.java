package com.energyxxer.craftr.files;

import com.energyxxer.craftr.ui.dialogs.file_dialogs.PackageDialog;
import com.energyxxer.craftr.ui.dialogs.file_dialogs.MCMETADialog;
import com.energyxxer.craftr.ui.dialogs.file_dialogs.ModelDialog;
import com.energyxxer.craftr.ui.dialogs.file_dialogs.ProjectDialog;
import com.energyxxer.craftr.ui.dialogs.file_dialogs.ResourceDialog;
import com.energyxxer.craftr.ui.dialogs.file_dialogs.UnitDialog;

/**
 * Created by User on 2/9/2017.
 */
public enum FileType {
    PROJECT("Project", ProjectDialog::create),
    ENTITY("Entity", UnitDialog::create),
    ITEM("Item", UnitDialog::create),
    CLASS("Class", UnitDialog::create),
    FEATURE("Feature", UnitDialog::create),
    WORLD("World", UnitDialog::create),
    MODEL("Model", ModelDialog::create),
    LANG("Language File", ResourceDialog::create),
    META("Meta File", MCMETADialog::create),
    PACKAGE("Package", PackageDialog::create);

    public final String name;
    public final FileTypeDialog dialog;

    FileType(String name, FileTypeDialog dialog) {
        this.name = name;
        this.dialog = dialog;
    }

    public void create(String destination) {
        this.dialog.create(this, destination);
    }

    @Override
    public String toString() {
        return name;
    }
}
