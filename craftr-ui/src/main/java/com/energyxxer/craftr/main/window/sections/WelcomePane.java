package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.dialogs.settings.Settings;
import com.energyxxer.craftr.ui.misc.TipScreen;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class WelcomePane extends JPanel {

    public final TipScreen tipScreen;

    ThemeListenerManager tlm = new ThemeListenerManager();

    private JPanel tipPanel = new JPanel(new BorderLayout());
    private JPanel contentPanel = new JPanel(new BorderLayout());
    private JPanel buttonPanel = new JPanel(new GridLayout(3,2));

    public WelcomePane() {
        super(new GridBagLayout());
        this.setOpaque(false);
        contentPanel.setOpaque(false);
        tipPanel.setOpaque(false);
        buttonPanel.setOpaque(false);

        this.add(contentPanel);
        contentPanel.add(tipPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        tipScreen = new TipScreen();
        tipPanel.add(tipScreen, BorderLayout.CENTER);

        tlm.addThemeChangeListener(t -> {
            tipScreen.setForeground(t.getColor("TipScreen.foreground", "General.foreground"));
            tipScreen.setFont(t.getFont("TipScreen","General"));
        });

        {
            ToolbarButton button = new ToolbarButton("project", tlm);
            button.setText("New Project");
            button.setHintText("Create a new project");
            button.addActionListener(e -> FileType.PROJECT.create(null));
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        {
            ToolbarButton button = new ToolbarButton(null, tlm);
            button.setText("Getting Started");
            button.setHintText("A guide on how to begin your Craftr project");
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        {
            ToolbarButton button = new ToolbarButton("cog", tlm);
            button.setText("Settings");
            button.setHintText("Manage settings");
            button.addActionListener(e -> Settings.show());
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        {
            ToolbarButton button = new ToolbarButton(null, tlm);
            button.setText("Documentation");
            button.setHintText("Read the language docs");
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        {
            ToolbarButton button = new ToolbarButton(null, tlm);
            button.setText("Select Workspace");
            button.setHintText("Choose a location to keep your projects");
            button.addActionListener(e -> Preferences.promptWorkspace());
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        {
            ToolbarButton button = new ToolbarButton("info", tlm);
            button.setText("About");
            button.setHintText("Learn about this build of Craftr");
            button.addActionListener(e -> AboutPane.INSTANCE.setVisible(true));
            JPanel wrapper = new JPanel();
            wrapper.setOpaque(false);
            wrapper.add(button);
            buttonPanel.add(wrapper);
        }
        //buttonPanel.add(new ToolbarButton("cog", tlm));
        //buttonPanel.add(new ToolbarButton("file", tlm));
        //buttonPanel.add(new ToolbarButton("model", tlm));
    }
}
