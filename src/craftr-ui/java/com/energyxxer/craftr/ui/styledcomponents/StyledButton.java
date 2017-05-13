package com.energyxxer.craftr.ui.styledcomponents;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.xswing.XButton;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;

/**
 * Provides a button that reacts to theme changes and
 * adjusts their own color. It also provides a namespace for
 * more specific paths on the theme file. If the namespace is
 * not specified, it defaults to the general style.
 */
public class StyledButton extends XButton {

    private String namespace = null;

    public StyledButton(String label) {
        this(label, null, null);
    }

    public StyledButton(String label, String namespace) {
        this(label, namespace, null);
    }

    public StyledButton(String label, ImageIcon icon) {
        this(label, null, icon);
    }

    public StyledButton(String label, String namespace, ImageIcon icon) {
        super(label, icon);
        if(namespace != null) this.setNamespace(namespace);

        ThemeChangeListener.addThemeChangeListener(t -> {
            if(this.namespace != null) {
                setBackground       (t.getColor(new Color(215, 215, 215), this.namespace + ".button.background","General.button.background"));
                setForeground       (t.getColor(Color.BLACK, this.namespace + ".button.foreground","General.button.foreground","General.foreground"));
                setBorder           (t.getColor(new Color(200, 200, 200), this.namespace + ".button.border.color","General.button.border.color"), Math.max(t.getInteger(1,this.namespace + ".button.border.thickness", "General.button.border.thickness"),0));
                setRolloverColor    (t.getColor(new Color(200, 202, 205), this.namespace + ".button.hover.background","General.button.hover.background"));
                setPressedColor     (t.getColor(Color.WHITE, this.namespace + ".button.pressed.background","General.button.pressed.background"));
                setFont(new Font   (t.getString(this.namespace + ".button.font","General.button.font","General.font","default:Tahoma"),
                        (t.getBoolean(false,this.namespace + ".button.bold", "General.button.bold") ? Font.BOLD : Font.PLAIN) +
                                (t.getBoolean(false,this.namespace + ".button.italic","General.button.italic") ? Font.ITALIC : Font.PLAIN),12));
            } else {
                setBackground       (t.getColor(new Color(215, 215, 215), "General.button.background"));
                setForeground       (t.getColor(Color.BLACK, "General.button.foreground","General.foreground"));
                setBorder(t.getColor(new Color(200, 200, 200), "General.button.border.color"),Math.max(t.getInteger(1,"General.button.border.thickness"),0));
                setRolloverColor    (t.getColor(new Color(200, 202, 205), "General.button.hover.background"));
                setPressedColor     (t.getColor(Color.WHITE, "General.button.pressed.background"));
                setFont(new Font   (t.getString("General.button.font","General.font","default:Tahoma"),
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
