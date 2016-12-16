package com.energyxxer.cbe.ui.styledcomponents;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

/**
 * Created by User on 12/15/2016.
 */
public class StyledLabel extends JLabel implements ThemeChangeListener {

    private String namespace = null;

    private int style = 0;
    private int size = 12;

    private String icon = null;

    private Theme theme;

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

        ThemeChangeListener.addThemeChangeListener(this);
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
        this.setFont(this.getFont().deriveFont(style));
    }

    public int getFontSize() {
        return size;
    }

    public void setFontSize(int size) {
        this.size = size;
        this.setFont(this.getFont().deriveFont((float) size));
    }

    public void setIconName(String icon) {
        this.icon = icon;
        themeChanged(theme);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void themeChanged(Theme t) {
        this.theme = t;
        if(this.namespace != null) {
            setForeground(t.getColor(this.namespace + ".label.foreground", t.getColor("General.label.foreground", t.getColor("General.foreground", Color.BLACK))));
            setFont(new Font(t.getString(this.namespace + "label.font",t.getString("General.label.font",t.getString("General.font","Tahoma"))),1,12));
        } else {
            setForeground(t.getColor("General.label.foreground", t.getColor("General.foreground", Color.BLACK)));
            setFont(new Font(t.getString("General.label.font",t.getString("General.font","Tahoma")),style,size));
        }
        if(icon != null) {
            this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + icon + ".png").getScaledInstance(16,16, Image.SCALE_SMOOTH)));
        } else {
            this.setIcon(null);
        }
    }
}
