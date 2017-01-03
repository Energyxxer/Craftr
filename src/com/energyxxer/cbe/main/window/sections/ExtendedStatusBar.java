package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.ui.styledcomponents.StyledLabel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Created by User on 1/3/2017.
 */
public class ExtendedStatusBar extends JPanel {

    StyledLabel caretInfo;

    {
        //this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //this.setAlignmentX(RIGHT_ALIGNMENT);
        //this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        this.setPreferredSize(new Dimension(300, 25));

        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));

        caretInfo = new StyledLabel(Commons.DEFAULT_CARET_DISPLAY_TEXT);
        //caretInfo.setPreferredSize(new Dimension(20,25));
        //caretInfo.setMinimumSize(new Dimension(20,25));
        //caretInfo.setMaximumSize(new Dimension(20,25));
        this.add(caretInfo);
    }

    public void setCaretInfo(String text) {
        caretInfo.setText(text);
    }
}
