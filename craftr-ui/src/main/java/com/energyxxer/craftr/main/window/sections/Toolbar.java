package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.ToolbarSeparator;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.util.out.Console;
import com.energyxxer.xswing.hints.TextHint;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Created by User on 12/15/2016.
 */
public class Toolbar extends JPanel {

    public TextHint hint = CraftrWindow.hintManager.createTextHint("");

    public ThemeListenerManager tlm;
    
    {
        this.tlm = new ThemeListenerManager();
        this.hint.setOutDelay(1);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(1, 30));
        tlm.addThemeChangeListener(t -> {
            this.setBackground(t.getColor(new Color(235, 235, 235), "Toolbar.background"));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"Toolbar.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "Toolbar.border.color")));
        });

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("project",tlm);
            button.setHintText("New Project");
            button.addActionListener(e -> FileType.PROJECT.create(null));
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save",tlm);
            button.setHintText("Save File");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save_all",tlm);
            button.setHintText("Save All Files");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("undo",tlm);
            button.setHintText("Undo");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("redo",tlm);
            button.setHintText("Redo");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("world",tlm);
            button.setHintText("New Global File");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("entity",tlm);
            button.setHintText("New Entity");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("item",tlm);
            button.setHintText("New Item");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("feature",tlm);
            button.setHintText("New Feature");
            this.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("class",tlm);
            button.setHintText("New Class");
            this.add(button);
        }

        this.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("export",tlm);
            button.setHintText("Generate Structure");
            button.addActionListener(e -> {
                if(Commons.getSelectedProject() == null) return;
                Compiler c = new Compiler(Commons.getSelectedProject());
                c.setLibrary(Resources.nativeLib);
                c.addProgressListener(CraftrWindow::setStatus);
                c.addCompletionListener(() -> {
                    CraftrWindow.noticeBoard.explorerMaster.setNotices(c.getReport().groupByLabel());
                    if(c.getReport().getTotal() > 0) CraftrWindow.noticeBoard.expand();
                    c.getReport().getWarnings().forEach(Console.warn::println);
                    c.getReport().getErrors().forEach(Console.err::println);
                });
                c.compile();

            });
            this.add(button);
        }
    }
}
