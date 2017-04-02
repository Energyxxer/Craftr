package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/14/2016.
 */
public class StyledPopupMenu extends JPopupMenu {

    private String namespace = null;

    public StyledPopupMenu() {
        this(null,null);
    }

    public StyledPopupMenu(String label) {
        this(label,null);
    }

    public StyledPopupMenu(String label, String namespace) {
        if(label != null) setLabel(label);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if (this.namespace != null) {
                setBackground(t.getColor(this.namespace + ".menu.background",t.getColor("General.menu.background",new Color(215, 215, 215))));
                int borderThickness = Math.max(t.getInteger(this.namespace + ".menu.border.thickness", t.getInteger("General.menu.border.thickness",1)),0);
                setBorder(BorderFactory.createMatteBorder(borderThickness, borderThickness, borderThickness, borderThickness, t.getColor(this.namespace + ".menu.border.color",t.getColor("General.menu.border.color",new Color(200, 200, 200)))));
            } else {
                setBackground(t.getColor("General.menu.background",new Color(215, 215, 215)));
                int borderThickness = Math.max(t.getInteger("General.menu.border.thickness",1),0);
                setBorder(BorderFactory.createMatteBorder(borderThickness, borderThickness, borderThickness, borderThickness ,t.getColor("General.menu.border.color",new Color(200, 200, 200))));
            }
        });
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void addSeparator() {
        this.add(new StyledSeparator(namespace));
    }
}
