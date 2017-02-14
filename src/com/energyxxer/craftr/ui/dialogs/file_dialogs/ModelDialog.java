package com.energyxxer.craftr.ui.dialogs.file_dialogs;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.main.window.Window;

import javax.swing.JDialog;

/**
 * Created by User on 2/10/2017.
 */
public class ModelDialog {

    private static JDialog dialog = new JDialog(Window.jframe);

    public static void create(FileType type, String destination) {
        dialog.setVisible(true);
    }
}
