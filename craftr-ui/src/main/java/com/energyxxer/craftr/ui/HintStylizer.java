package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.xswing.hints.Hint;
import com.energyxxer.xswing.hints.TextHint;

import java.awt.Color;

public class HintStylizer {
    public static void style(Hint hint, String type) {
        Theme t = CraftrWindow.getTheme();

        hint.setBackgroundColor(t.getColor(Color.BLACK, "Hint."+type+".background","Hint.background"));
        hint.setBorderColor(t.getColor(Color.WHITE, "Hint."+type+".border","Hint.border"));
        if(hint instanceof TextHint) {
            TextHint thint = (TextHint) hint;
            thint.setForeground(t.getColor(Color.WHITE, "Hint."+type+".foreground","Hint.foreground","General.foreground"));
            thint.setBold(t.getBoolean(false, "Hint."+type+".bold","Hint.bold"));
            thint.setItalic(t.getBoolean(false, "Hint."+type+".italic","Hint.italic"));
            thint.setFontSize(t.getInteger(12, "Hint."+type+".fontSize","Hint.fontSize","General.fontSize"));
            thint.setFontFamily(t.getString("Tahoma", "Hint."+type+".font","Hint.font","General.font"));
        }
    }
}
