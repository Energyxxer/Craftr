package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.ToolbarSeparator;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.util.out.Console;
import com.energyxxer.xswing.Padding;
import com.energyxxer.xswing.hints.TextHint;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Created by User on 12/15/2016.
 */
public class Toolbar extends JPanel {

    public TextHint hint = CraftrWindow.hintManager.createTextHint("");
    public StyledLabel projectLabel;

    public ThemeListenerManager tlm;

    public void setActiveProject(Project project) {
        if(project != null) {
            projectLabel.setText(project.getName());
            projectLabel.setIconName("project");
        } else {
            projectLabel.setText("");
            projectLabel.setIconName(null);
        }
    }
    
    {
        this.tlm = new ThemeListenerManager();
        this.hint.setOutDelay(1);

        int defaultHeight = 29;

        this.setPreferredSize(new Dimension(1, defaultHeight));
        this.setLayout(new BorderLayout());

        JPanel projectIndicator = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, (defaultHeight-16)/2-2));
        projectIndicator.setOpaque(false);
        this.add(projectIndicator, BorderLayout.CENTER);

        projectIndicator.add(new Padding(10));
        projectLabel = new StyledLabel("", "Toolbar.projectIndicator");
        projectLabel.setTextChangeable(false);
        projectIndicator.add(projectLabel);

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, (defaultHeight-ToolbarButton.SIZE)/2-2));
        tlm.addThemeChangeListener(t -> {
            this.setBackground(t.getColor(new Color(235, 235, 235), "Toolbar.background"));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, Math.max(t.getInteger(1,"Toolbar.border.thickness"),0), 0, t.getColor(new Color(200, 200, 200), "Toolbar.border.color")));

            int height = t.getInteger(29, "Toolbar.height");

            this.setPreferredSize(new Dimension(1, height));
            ((FlowLayout) buttonBar.getLayout()).setVgap((height-ToolbarButton.SIZE)/2-2);
            ((FlowLayout) projectIndicator.getLayout()).setVgap((height-16)/2-2);
        });
        buttonBar.setOpaque(false);
        this.add(buttonBar, BorderLayout.EAST);
        
        buttonBar.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("project",tlm);
            button.setHintText("New Project");
            button.addActionListener(e -> FileType.PROJECT.create(null));
            buttonBar.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save",tlm);
            button.setHintText("Save File");
            buttonBar.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("save_all",tlm);
            button.setHintText("Save All Files");
            buttonBar.add(button);
        }

        buttonBar.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("world",tlm);
            button.setHintText("New Global Unit");
            buttonBar.add(button);
        }

        buttonBar.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("entity",tlm);
            button.setHintText("New Entity");
            buttonBar.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("item",tlm);
            button.setHintText("New Item");
            buttonBar.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("feature",tlm);
            button.setHintText("New Feature");
            buttonBar.add(button);
        }

        {
            ToolbarButton button = new ToolbarButton("class",tlm);
            button.setHintText("New Class");
            buttonBar.add(button);
        }

        buttonBar.add(new ToolbarSeparator());

        {
            ToolbarButton button = new ToolbarButton("export",tlm);
            button.setHintText("Compile");
            button.addActionListener(e -> {
                if(Commons.getActiveProject() == null) return;
                Compiler c = new Compiler(Commons.getActiveProject());
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
            buttonBar.add(button);
        }

        buttonBar.add(new Padding(10));
    }
}
