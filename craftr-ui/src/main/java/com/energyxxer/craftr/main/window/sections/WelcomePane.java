package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.styledcomponents.StyledLabel;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

public class WelcomePane extends JPanel {

    ThemeListenerManager tlm = new ThemeListenerManager();

    private JPanel tipPanel = new JPanel(new BorderLayout());
    private JPanel contentPanel = new JPanel(new BorderLayout());
    private GridBagLayout buttonPanelLayout = new GridBagLayout();
    private JPanel buttonPanel = new JPanel(buttonPanelLayout);

    public WelcomePane() {
        super(new GridBagLayout());
        this.setOpaque(false);
        contentPanel.setOpaque(false);
        tipPanel.setOpaque(false);
        buttonPanel.setOpaque(false);

        this.add(contentPanel);
        contentPanel.add(tipPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        StyledLabel tipLabel = new StyledLabel("HELLO THERE", JLabel.CENTER);
        tipLabel.setTextChangeable(false);
        tipPanel.add(tipLabel, BorderLayout.CENTER);

        buttonPanel.add(new ToolbarButton("project", tlm));
        buttonPanel.add(new ToolbarButton("cog", tlm));
        buttonPanel.add(new ToolbarButton("file", tlm));
        buttonPanel.add(new ToolbarButton("model", tlm));
    }
}
