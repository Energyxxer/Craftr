package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.compile.Compiler;
import com.energyxxer.cbe.ui.ToolbarButton;
import com.energyxxer.cbe.ui.ToolbarSeparator;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/15/2016.
 */
public class Toolbar extends JPanel {
    {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(1, 30));
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor("Toolbar.background",new Color(235, 235, 235)));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("Toolbar.border",new Color(200, 200, 200))));
        });

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("project_new");
            button.setToolTipText("New Project");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save");
            button.setToolTipText("Save File");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save_all");
            button.setToolTipText("Save All Files");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("undo");
            button.setToolTipText("Undo");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("redo");
            button.setToolTipText("Redo");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("world");
            button.setToolTipText("New Global File");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("entity_new");
            button.setToolTipText("New Entity");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("item_new");
            button.setToolTipText("New Item");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("feature_new");
            button.setToolTipText("New Feature");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("class_new");
            button.setToolTipText("New Class");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("export");
            button.setToolTipText("Generate Structure");
            button.addActionListener(e -> Compiler.compile());
            this.add(button);
        }
    }
}
