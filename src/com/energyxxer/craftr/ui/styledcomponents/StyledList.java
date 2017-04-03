package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.components.XList;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

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
                        0, 0, Math.max(t.getInteger(1,this.namespace + ".list.cell.selected.border.thickness","General.list.cell.selected.border.thickness"),0), 0,
                                            t.getColor(this.namespace + ".list.cell.selected.border.color",       t.getColor("General.list.cell.selected.border.color",     new Color(0, 0, 0, 0)))));

                setRolloverCellBackground(  t.getColor(this.namespace + ".list.cell.hover.background",      t.getColor("General.list.cell.hover.background",    new Color(235, 235, 235))));
                setRolloverCellBorder(
                        BorderFactory.createMatteBorder(
                        0, 0, Math.max(t.getInteger(1,this.namespace + ".list.cell.rollover.border.thickness","General.list.cell.rollover.border.thickness"),0), 0,
                                            t.getColor(this.namespace + ".list.cell.hover.border.color",          t.getColor("General.list.cell.hover.border.color",        new Color(0, 0, 0, 0)))));

                setFont(new Font(          t.getString(this.namespace + ".list.font","General.list.font","General.font","default:Tahoma"),0,14));
            } else {
                setBackground(              t.getColor("General.list.background",               Color.WHITE));
                setForeground(              t.getColor("General.list.cell.foreground",          t.getColor("General.foreground", Color.BLACK)));

                setCellBackground(          t.getColor("General.list.cell.background",          new Color(215, 215, 215)));

                setSelectedCellBackground(  t.getColor("General.list.cell.selected.background", new Color(235, 235, 235)));
                setSelectedCellBorder(
                        BorderFactory.createMatteBorder(
                        0, 0, Math.max(t.getInteger(1,"General.list.cell.selected.border.thickness"),0), 0,
                                t.getColor("General.list.cell.selected.border",     new Color(0, 0, 0, 0))));

                setRolloverCellBackground(  t.getColor("General.list.cell.hover.background",    new Color(235, 235, 235)));
                setRolloverCellBorder(
                        BorderFactory.createMatteBorder(
                        0, 0, Math.max(t.getInteger(1,"General.list.cell.rollover.border.thickness"),0), 0,
                                t.getColor("General.list.cell.hover.border",        new Color(0, 0, 0, 0))));

                setFont(new Font(t.getString("General.list.font","General.font","default:Tahoma"),0,14));
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
