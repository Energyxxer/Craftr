package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/15/2016.
 */
public class StyledLabel extends JLabel {

    private String namespace = null;

    public StyledLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        setNamespaceInit(null);
    }

    public StyledLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setNamespaceInit(null);
    }

    public StyledLabel(String text) {
        super(text);
        setNamespaceInit(null);
    }

    public StyledLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        setNamespaceInit(null);
    }

    public StyledLabel(Icon image) {
        super(image);
        setNamespaceInit(null);
    }

    public StyledLabel() {
        setNamespaceInit(null);
    }

    //New

    public StyledLabel(String text, String namespace, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        setNamespaceInit(namespace);
    }

    public StyledLabel(String text, String namespace, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setNamespaceInit(namespace);
    }

    public StyledLabel(String text, String namespace) {
        super(text);
        setNamespaceInit(namespace);
    }

    public StyledLabel(Icon image, String namespace, int horizontalAlignment) {
        super(image, horizontalAlignment);
        setNamespaceInit(namespace);
    }

    public StyledLabel(Icon image, String namespace) {
        super(image);
        setNamespaceInit(namespace);
    }

    private void setNamespaceInit(String namespace) {
        setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setForeground(t.getColor(this.namespace + ".label.foreground", t.getColor("General.label.foreground", t.getColor("General.foreground", Color.BLACK))));
                setFont(new Font(t.getString(this.namespace + "label.font",t.getString("General.label.font",t.getString("General.font","Tahoma"))),1,12));
            } else {
                setForeground(t.getColor("General.label.foreground", t.getColor("General.foreground", Color.BLACK)));
                setFont(new Font(t.getString("General.label.font",t.getString("General.font","Tahoma")),1,12));
            }
        });
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
