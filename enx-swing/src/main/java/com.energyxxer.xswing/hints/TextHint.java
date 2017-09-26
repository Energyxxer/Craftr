package com.energyxxer.xswing.hints;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;

public class TextHint extends Hint {
    private final JTextPane textPane;

    public TextHint(JFrame owner) {
        super(owner);
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setBackground(new Color(0,0,0,0));
        textPane.setForeground(new Color(187, 187, 187));
        this.setContent(textPane);
    }

    public TextHint(JFrame owner, String text) {
        this(owner);
        setText(text);
    }

    public void setText(String text) {
        textPane.setText(text);
        textPane.invalidate();
        textPane.validate();
        this.update();
    }

    public Color getForeground() {
        return textPane.getForeground();
    }

    public void setForeground(Color fg) {
        textPane.setForeground(fg);
    }

    public boolean isBold() {
        return (textPane.getFont().getStyle() & Font.BOLD) > 0;
    }

    public void setBold(boolean bold) {
        //I believe this to be the most confusing code I've ever written.
        if(bold) textPane.setFont(textPane.getFont().deriveFont(textPane.getFont().getStyle() | Font.BOLD));
        else textPane.setFont(textPane.getFont().deriveFont(textPane.getFont().getStyle() & ~Font.BOLD));
    }

    public boolean isItalic() {
        return (textPane.getFont().getStyle() & Font.ITALIC) > 0;
    }

    public void setItalic(boolean italic) {
        //Darn bitwise operators.
        if(italic) textPane.setFont(textPane.getFont().deriveFont(textPane.getFont().getStyle() | Font.ITALIC));
        else textPane.setFont(textPane.getFont().deriveFont(textPane.getFont().getStyle() & ~Font.ITALIC));
    }

    public String getFontFamily() {
        return textPane.getFont().getFamily();
    }

    public void setFontFamily(String family) {
        textPane.setFont(new Font(family, textPane.getFont().getStyle(),textPane.getFont().getSize()));
    }

    public int getFontSize() {
        return textPane.getFont().getSize();
    }

    public void setFontSize(int size) {
        textPane.setFont(textPane.getFont().deriveFont((float) size));
    }
}
