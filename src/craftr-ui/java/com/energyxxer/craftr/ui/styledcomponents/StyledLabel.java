package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

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

    private String defaultText = "";

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
        this.defaultText = text;
        setNamespaceInit(namespace);
    }

    public StyledLabel(String text, String namespace, int horizontalAlignment) {
        super(text, horizontalAlignment);
        this.defaultText = text;
        setNamespaceInit(namespace);
    }

    public StyledLabel(String text, String namespace) {
        super(text);
        this.defaultText = text;
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
        this.update();
        revalidate();
        repaint();
    }

    public int getFontSize() {
        return size;
    }

    public void setFontSize(int size) {
        this.size = size;
        this.update();
        revalidate();
        repaint();
    }

    public void setIconName(String icon) {
        this.icon = icon;
        this.update();
        revalidate();
        repaint();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void themeChanged(Theme t0) {
        this.theme = t0;
        this.update();
    }

    private void update() {
        Theme t = this.theme;

        if (this.namespace != null) {
            setForeground(t.getColor(Color.BLACK, this.namespace + ".label.foreground","General.label.foreground","General.foreground"));
            setFont(new Font(t.getString(this.namespace + ".label.font","General.label.font","General.font", "default:Tahoma"),
                    (t.getBoolean((style & Font.BOLD) > 0,this.namespace + ".label.bold", "General.label.bold") ? Font.BOLD : Font.PLAIN) +
                    (t.getBoolean((style & Font.ITALIC) > 0,this.namespace + ".label.italic","General.label.italic") ? Font.ITALIC : Font.PLAIN),
                    t.getInteger(this.size,this.namespace + ".label.fontSize","General.label.fontSize")));
            this.setText(t.getString(this.namespace + ".label.text","default:" + defaultText));
        } else {
            setForeground(t.getColor(Color.BLACK, "General.label.foreground","General.foreground"));
            setFont(new Font(t.getString("General.label.font","General.font", "default:Tahoma"),
                    (t.getBoolean((style & Font.BOLD) > 0, "General.label.bold") ? Font.BOLD : Font.PLAIN) +
                            (t.getBoolean((style & Font.ITALIC) > 0,"General.label.italic") ? Font.ITALIC : Font.PLAIN),
                    t.getInteger(this.size,"General.label.fontSize")));
            this.setText(t.getString(defaultText));
        }
        if (icon != null) {
            this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } else {
            this.setIcon(null);
        }
    }
}
