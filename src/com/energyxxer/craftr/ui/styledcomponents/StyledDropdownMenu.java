package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.components.XDropdownMenu;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import java.awt.Color;
import java.awt.Font;

/**
 * Created by User on 2/11/2017.
 */
public class StyledDropdownMenu<T> extends XDropdownMenu<T> {

    private String namespace = null;

    public StyledDropdownMenu() {
    }

    public StyledDropdownMenu(String namespace) {
        this.namespace = namespace;
    }

    public StyledDropdownMenu(T[] options) {
        super(options);
    }

    public StyledDropdownMenu(T[] options, String namespace) {
        super(options);
        this.namespace = namespace;
    }

    {
        this.setPopupFactory(StyledPopupMenu::new);
        this.setPopupItemFactory(StyledMenuItem::new);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground       (t.getColor(this.namespace + ".dropdown.background",          t.getColor("General.dropdown.background",         new Color(215, 215, 215))));
                setForeground       (t.getColor(this.namespace + ".dropdown.foreground",          t.getColor("General.dropdown.foreground",         t.getColor("General.foreground", Color.BLACK))));
                setBorder(t.getColor(this.namespace + ".dropdown.border.color",              t.getColor("General.dropdown.border.color",             new Color(200, 200, 200))), Math.max(t.getInteger(1,this.namespace + ".dropdown.border.thickness","General.dropdown.border.thickness"),0));
                setRolloverColor    (t.getColor(this.namespace + ".dropdown.hover.background",    t.getColor("General.dropdown.hover.background",   new Color(200, 202, 205))));
                setPressedColor     (t.getColor(this.namespace + ".dropdown.pressed.background",  t.getColor("General.dropdown.pressed.background", Color.WHITE)));
                setFont(new Font   (t.getString(this.namespace + ".dropdown.font","General.dropdown.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,this.namespace + ".dropdown.bold","General.dropdown.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,this.namespace + ".dropdown.italic","General.dropdown.italic") ? Font.ITALIC : Font.PLAIN),12));
            } else {
                setBackground       (t.getColor("General.dropdown.background",         new Color(215, 215, 215)));
                setForeground       (t.getColor("General.dropdown.foreground",         t.getColor("General.foreground", Color.BLACK)));
                setBorder(t.getColor("General.dropdown.border.color",             new Color(200, 200, 200)), Math.max(t.getInteger(1,"General.dropdown.border.thickness"),0));
                setRolloverColor    (t.getColor("General.dropdown.hover.background",   new Color(200, 202, 205)));
                setPressedColor     (t.getColor("General.dropdown.pressed.background", Color.WHITE));
                setFont(new Font   (t.getString("General.dropdown.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,"General.dropdown.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,"General.dropdown.italic") ? Font.ITALIC : Font.PLAIN),
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
