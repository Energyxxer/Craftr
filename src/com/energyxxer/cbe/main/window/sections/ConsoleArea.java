package com.energyxxer.cbe.main.window.sections;

import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.ui.ToolbarButton;
import com.energyxxer.cbe.ui.scrollbar.ScrollbarUI;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.out.MultiOutputStream;
import com.energyxxer.cbe.util.out.TextAreaOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.PrintStream;

/**
 * Created by User on 12/15/2016.
 */
public class ConsoleArea extends JPanel {

    private static final int CONSOLE_HEIGHT = 200;
    public PrintStream consoleOut = new PrintStream(System.out);
    private TextAreaOutputStream textConsoleOut = null;

    {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
        ThemeChangeListener.addThemeChangeListener(t -> this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200)))));

        JPanel consoleHeader = new JPanel(new BorderLayout());
        ThemeChangeListener.addThemeChangeListener(t -> consoleHeader.setBackground(t.getColor("Console.header.background",new Color(235, 235, 235))));
        consoleHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        consoleHeader.setPreferredSize(new Dimension(0, 25));

        JLabel consoleLabel = new JLabel("Java Console");
        ThemeChangeListener.addThemeChangeListener(t -> {
            consoleLabel.setForeground(t.getColor("Console.header.foreground",Color.BLACK));
            consoleLabel.setFont(new Font(t.getString("Console.header.font",t.getString("General.font","Tahoma")), 0, 12));
        });
        consoleHeader.add(consoleLabel, BorderLayout.WEST);

        JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        consoleActionPanel.setOpaque(false);

        ToolbarButton toggle = new ToolbarButton("toggle", true);
        toggle.setToolTipText("Toggle Java Console");
        toggle.setPreferredSize(new Dimension(20,20));

        toggle.addActionListener(e -> {
            if (this.getPreferredSize().height == 25) {
                this.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
            } else {
                this.setPreferredSize(new Dimension(0, 25));
            }
            this.revalidate();
            this.repaint();
        });


        ToolbarButton clear = new ToolbarButton("clear", true);
        clear.setToolTipText("Clear Console");
        clear.setPreferredSize(new Dimension(20,20));

        clear.addActionListener(e -> textConsoleOut.clear());

        consoleActionPanel.add(clear);
        consoleActionPanel.add(toggle);
        consoleHeader.add(consoleActionPanel, BorderLayout.EAST);

        this.add(consoleHeader, BorderLayout.NORTH);

        JEditorPane console = new JEditorPane();
        ThemeChangeListener.addThemeChangeListener(t -> {
            console.setBackground(t.getColor("Console.background",Color.WHITE));
            console.setSelectionColor(t.getColor("Console.selection.background",t.getColor("General.textfield.selection.background",new Color(50, 100, 175))));
            console.setSelectedTextColor(t.getColor("Console.selection.foreground",t.getColor("General.textfield.selection.foreground",t.getColor("Console.foreground",t.getColor("General.foreground",Color.BLACK)))));
            console.setFont(new Font(t.getString("Console.font",t.getString("Editor.font","monospaced")), 0, 12));
        });
        console.setEditable(false);
        console.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        console.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

        console.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                String path = e.getURL().toString().substring(7, e.getURL().toString().lastIndexOf('?'));
                String location = e.getURL().toString().substring(e.getURL().toString().lastIndexOf('?') + 1);
                String length = location.substring(location.indexOf('&')+1);
                location = location.substring(0,location.indexOf('&'));
                TabManager.openTab(path, Integer.parseInt(location.split(":")[0]),
                        Integer.parseInt(location.split(":")[1]), Integer.parseInt(length));
            }
        });

        textConsoleOut = new TextAreaOutputStream(console);

        ThemeChangeListener.addThemeChangeListener(t -> textConsoleOut.update());

        consoleOut = new PrintStream(textConsoleOut);
        System.setOut(new PrintStream(new MultiOutputStream(consoleOut, System.out)));
        System.setErr(new PrintStream(new MultiOutputStream(consoleOut, System.err)));

        JScrollPane consoleScrollPane = new JScrollPane(console);

        //consoleScrollPane.setLayout(new OverlayScrollPaneLayout());

        consoleScrollPane.getVerticalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
        consoleScrollPane.getHorizontalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
        consoleScrollPane.getVerticalScrollBar().setOpaque(false);
        consoleScrollPane.getHorizontalScrollBar().setOpaque(false);

        ThemeChangeListener.addThemeChangeListener(t -> {
            consoleScrollPane.setBackground(console.getBackground());
            consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200))));
        });

        this.add(consoleScrollPane, BorderLayout.CENTER);

    }
}
