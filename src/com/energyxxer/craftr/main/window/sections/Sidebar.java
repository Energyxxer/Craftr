package com.energyxxer.craftr.main.window.sections;

import com.energyxxer.craftr.main.window.Window;
import com.energyxxer.craftr.ui.ToolbarButton;
import com.energyxxer.craftr.ui.explorer.Explorer;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 12/15/2016.
 */
public class Sidebar extends JPanel {

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350, 500));
        ThemeChangeListener.addThemeChangeListener(t -> {
            this.setBackground(t.getColor("Explorer.background",Color.WHITE));
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.getColor("Explorer.border",new Color(200, 200, 200))));
        });

        JPanel header = new JPanel(new BorderLayout());
        ThemeChangeListener.addThemeChangeListener(t -> header.setBackground(this.getBackground()));

        JLabel label = new JLabel("    Project Explorer");
        ThemeChangeListener.addThemeChangeListener(t -> {
            label.setFont(new Font(t.getString("Explorer.header.font",t.getString("General.font","Tahoma")), Font.PLAIN, 14));
            label.setForeground(t.getColor("Explorer.header.foreground",t.getColor("General.foreground",Color.BLACK)));
        });
        label.setPreferredSize(new Dimension(500, 25));
        header.add(label, BorderLayout.WEST);

        ToolbarButton refresh = new ToolbarButton("reload");
        refresh.setToolTipText("Refresh Explorer");

        refresh.addActionListener(e -> Window.explorer.generateProjectList());

        header.add(refresh, BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane();
        sp.getViewport().setBackground(Color.BLACK);

        sp.getViewport().add(Window.explorer = new Explorer());
        ThemeChangeListener.addThemeChangeListener(t -> {
            sp.setBackground(t.getColor("Explorer.background",Color.WHITE));
            sp.setBorder(BorderFactory.createEmptyBorder());
        });

        this.add(sp, BorderLayout.CENTER);
    }
}
