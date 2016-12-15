package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.ui.components.XList;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/14/2016.
 */
public class StyledList<T> extends XList<T> {

    private String namespace = null;

    public StyledList() {
        this(null, null);
    }

    public StyledList(T[] options) {
        this(options, null);
    }

    public StyledList(T[] options, String namespace) {
        if(options != null) setOptions(options);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground(              t.getColor(this.namespace + ".list.background",                 t.getColor("General.list.background",               Color.WHITE)));
                setForeground(              t.getColor(this.namespace + ".list.cell.foreground",            t.getColor("General.list.cell.foreground",          t.getColor("General.foreground", Color.BLACK))));

                setCellBackground(          t.getColor(this.namespace + ".list.cell.background",            t.getColor("General.list.cell.background",          new Color(215, 215, 215))));

                setSelectedCellBackground(  t.getColor(this.namespace + ".list.cell.selected.background",   t.getColor("General.list.cell.selected.background", new Color(235, 235, 235))));
                setSelectedCellBorder(
                        BorderFactory.createMatteBorder(
                        1, 0, 1, 0,
                                            t.getColor(this.namespace + ".list.cell.selected.border",       t.getColor("General.list.cell.selected.border",     new Color(200, 200, 200)))));

                setRolloverCellBackground(  t.getColor(this.namespace + ".list.cell.hover.background",      t.getColor("General.list.cell.hover.background",    new Color(235, 235, 235))));
                setRolloverCellBorder(
                        BorderFactory.createMatteBorder(
                        1, 0, 1, 0,
                                            t.getColor(this.namespace + ".list.cell.hover.border",          t.getColor("General.list.cell.hover.border",        new Color(200, 200, 200)))));

                setFont(new Font(          t.getString(this.namespace + ".list.font",                      t.getString("General.list.font", t.getString("General.font","Tahoma"))),0,14));
            } else {
                setBackground(              t.getColor("General.list.background",               Color.WHITE));
                setForeground(              t.getColor("General.list.cell.foreground",          t.getColor("General.foreground", Color.BLACK)));

                setCellBackground(          t.getColor("General.list.cell.background",          new Color(215, 215, 215)));

                setSelectedCellBackground(  t.getColor("General.list.cell.selected.background", new Color(235, 235, 235)));
                setSelectedCellBorder(
                        BorderFactory.createMatteBorder(
                        1, 0, 1, 0,
                                t.getColor("General.list.cell.selected.border",     new Color(200, 200, 200))));

                setRolloverCellBackground(  t.getColor("General.list.cell.hover.background",    new Color(235, 235, 235)));
                setRolloverCellBorder(
                        BorderFactory.createMatteBorder(
                        1, 0, 1, 0,
                                t.getColor("General.list.cell.hover.border",        new Color(200, 200, 200))));

                setFont(new Font(          t.getString("General.list.font", t.getString("General.font","Tahoma")),0,14));
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
