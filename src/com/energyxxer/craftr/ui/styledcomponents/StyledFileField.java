package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.components.XButton;
import com.energyxxer.craftr.ui.components.XFileField;
import com.energyxxer.craftr.ui.components.XTextField;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import java.awt.Color;
import java.awt.Font;
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
                field.setBorder(t.getColor(this.namespace + ".textfield.border.color",               t.getColor("General.textfield.border.color",              new Color(200, 200, 200))),Math.max(t.getInteger(1,this.namespace + ".textfield.border.borderThickness","General.textfield.border.borderThickness"),0));
                field.setFont(new Font   (t.getString(this.namespace + ".textfield.font","General.textfield.font","General.font","default:Tahoma"),0,12));

                button.setBackground       (t.getColor(this.namespace + ".button.background",          t.getColor("General.button.background",         new Color(215, 215, 215))));
                button.setForeground       (t.getColor(this.namespace + ".button.foreground",          t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK))));
                button.setBorder(t.getColor(this.namespace + ".button.border.color",              t.getColor("General.button.border.color",             new Color(200, 200, 200))), Math.max(t.getInteger(1,this.namespace + ".button.border.thickness","General.button.border.thickness"),0));
                button.setRolloverColor    (t.getColor(this.namespace + ".button.hover.background",    t.getColor("General.button.hover.background",   new Color(200, 202, 205))));
                button.setPressedColor     (t.getColor(this.namespace + ".button.pressed.background",  t.getColor("General.button.pressed.background", Color.WHITE)));
                button.setFont(new Font   (t.getString(this.namespace + ".button.font","General.button.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,this.namespace + ".button.bold", "General.button.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,this.namespace + ".button.italic", "General.button.italic") ? Font.ITALIC : Font.PLAIN),12));
            } else {
                field.setBackground       (t.getColor("General.textfield.background",          new Color(220, 220, 220)));
                field.setForeground       (t.getColor("General.textfield.foreground",          t.getColor("General.foreground", Color.BLACK)));
                field.setSelectionColor   (t.getColor("General.textfield.selection.background",new Color(50, 100, 175)));
                field.setSelectedTextColor(t.getColor("General.textfield.selection.foreground",field.getForeground()));
                field.setBorder(t.getColor("General.textfield.border.color",              new Color(200, 200, 200)),Math.max(t.getInteger(1,"General.textfield.border.borderThickness"),0));
                field.setFont(new Font   (t.getString("General.textfield.font","General.font","default:Tahoma"),0,12));

                button.setBackground       (t.getColor("General.button.background",         new Color(215, 215, 215)));
                button.setForeground       (t.getColor("General.button.foreground",         t.getColor("General.foreground", Color.BLACK)));
                button.setBorder(t.getColor("General.button.border.color",             new Color(200, 200, 200)), Math.max(t.getInteger(1,"General.button.border.thickness"),0));
                button.setRolloverColor    (t.getColor("General.button.hover.background",   new Color(200, 202, 205)));
                button.setPressedColor     (t.getColor("General.button.pressed.background", Color.WHITE));
                button.setFont(new Font   (t.getString("General.button.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,"General.button.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,"General.button.italic") ? Font.ITALIC : Font.PLAIN),
                        12));
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
