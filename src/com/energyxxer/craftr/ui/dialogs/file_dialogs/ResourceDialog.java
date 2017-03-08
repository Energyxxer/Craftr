package com.energyxxer.craftr.ui.dialogs.file_dialogs;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.main.window.CraftrWindow;

import javax.swing.JDialog;

/**
 * Created by User on 2/10/2017.
 */
public class ResourceDialog {

    private static JDialog dialog = new JDialog(CraftrWindow.jframe);

    public static void create(FileType type, String destination) {
        dialog.setVisible(true);
    }
}
