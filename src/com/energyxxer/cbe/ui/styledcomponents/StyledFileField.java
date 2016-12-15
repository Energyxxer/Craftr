package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.ui.components.XButton;
import com.energyxxer.cbe.ui.components.XFileField;
import com.energyxxer.cbe.ui.components.XTextField;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import java.awt.*;
import java.io.File;

/**
 * Provides a file field that reacts to theme changes and
 * adjusts their own color. It also provides a namespace for
 * more specific paths on the theme file. If the namespace is
 * not specified, it defaults to the general style.
 */
public class StyledFileField extends XFileField {

    private String namespace = null;

    public StyledFileField() {
        this((byte) -1, null, null);
    }

    public StyledFileField(String namespace) {
        this((byte) -1, null, namespace);
    }

    public StyledFileField(byte operation) {
        this(operation, null, null);
    }

    public StyledFileField(byte operation, String namespace) {
        this(operation, null, namespace);
    }

    public StyledFileField(File file) {
        this((byte) -1, file, null);
    }

    public StyledFileField(File file, String namespace) {
        this((byte) -1, file, namespace);
    }

    public StyledFileField(byte operation, File file) {
        this(operation, file, null);
    }

    public StyledFileField(byte operation, File file, String namespace) {
        if(operation >= 0) setOperation(operation);
        if(file != null) setFile(file);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {

            XTextField field = this.getField();
            XButton button = this.getButton();
            
            if(this.namespace != null) {
                field.setBackground       (t.getColor(this.namespace + ".textfield.background",           t.getColor("General.textfield.background",          new Color(220, 220, 220))));
                field.setForeground       (t.getColor(this.namespace + ".textfield.foreground",           t.getColor("General.textfield.foreground",          t.getColor("General.foreground", Color.BLACK))));
                field.setSelectionColor   (t.getColor(this.namespace + ".textfield.selection.background", t.getColor("General.textfield.selection.background",new Color(50, 100, 175))));
                field.setSelectedTextColor(t.getColor(this.namespace + ".textfield.selection.foreground", t.getColor("General.textfield.selection.foreground",field.getForeground())));
                field.setBorderColor      (t.getColor(this.namespace + ".textfield.border",               t.getColor("General.textfield.border",              new Color(200, 200, 200))));
                field.setFont(new Font   (t.getString(this.namespace + ".textfield.font",                t.getString("General.textfield.font",                t.getString("General.font","Tahoma"))),0,12));

                button.setBackground       (t.getColor(this.namespace + ".button.background",          t.getColor("General.button.background",         new Color(215, 215, 215))));
                button.setForeground       (t.getColor(this.namespace + ".button.foreground",          t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK))));
                button.setBorderColor      (t.getColor(this.namespace + ".button.border",              t.getColor("General.button.border",             new Color(200, 200, 200))));
                button.setRolloverColor    (t.getColor(this.namespace + ".button.hover.background",    t.getColor("General.button.hover.background",   new Color(200, 202, 205))));
                button.setPressedColor     (t.getColor(this.namespace + ".button.pressed.background",  t.getColor("General.button.pressed.background", Color.WHITE)));
                button.setFont(new Font   (t.getString(this.namespace + ".button.font",               t.getString("General.button.font",               t.getString("General.font","Tahoma"))),0,12));
            } else {
                field.setBackground       (t.getColor("General.textfield.background",          new Color(220, 220, 220)));
                field.setForeground       (t.getColor("General.textfield.foreground",          t.getColor("General.foreground", Color.BLACK)));
                field.setSelectionColor   (t.getColor("General.textfield.selection.background",new Color(50, 100, 175)));
                field.setSelectedTextColor(t.getColor("General.textfield.selection.foreground",field.getForeground()));
                field.setBorderColor      (t.getColor("General.textfield.border",              new Color(200, 200, 200)));
                field.setFont(new Font   (t.getString("General.textfield.font",                t.getString("General.font","Tahoma")),0,12));

                button.setBackground       (t.getColor("General.button.background",         new Color(215, 215, 215)));
                button.setForeground       (t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK)));
                button.setBorderColor      (t.getColor("General.button.border",             new Color(200, 200, 200)));
                button.setRolloverColor    (t.getColor("General.button.hover.background",   new Color(200, 202, 205)));
                button.setPressedColor     (t.getColor("General.button.pressed.background", Color.WHITE));
                button.setFont(new Font   (t.getString("General.button.font",               t.getString("General.font","Tahoma")),0,12));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }


}
