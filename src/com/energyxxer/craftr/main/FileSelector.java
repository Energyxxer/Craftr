package com.energyxxer.craftr.main;

import com.energyxxer.craftr.main.window.Window;
import com.energyxxer.craftr.util.Range;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Allows the user to choose a workspace location for their projects.
 */
public class FileSelector {

    protected String value = null;

    protected JPanel dialog = new JPanel(new BorderLayout());
    protected JPanel instructions = new JPanel();
    protected JPanel input = new JPanel(new BorderLayout());
    protected JTextField textfield = new JTextField();
    protected JButton browse = new JButton("Browse...");

    protected JButton okay = new JButton("OK");
    protected JButton cancel = new JButton("Cancel");

    public static final byte OPEN_ALL = 0;
    public static final byte OPEN_FILE = 1;
    public static final byte OPEN_DIRECTORY = 2;
    public static final byte SAVE = 3;

    public static String create(String windowLabel, String instructionText, String defaultPath, byte operation) {
        return new FileSelector(windowLabel, instructionText, defaultPath, operation).value;
    }

    private FileSelector(String windowLabel, String instructionText, String defaultPath, byte operation) {

        if(!new Range(OPEN_ALL, SAVE).contains(operation)) {
            throw new RuntimeException(operation + " is not a valid FileSelector operation constant.");
        }

        instructions.setPreferredSize(new Dimension(400, 50));

        instructions.add(new JLabel(instructionText),FlowLayout.LEFT);
        dialog.add(instructions, BorderLayout.NORTH);

        input.setPreferredSize(new Dimension(400, 30));

        textfield.setPreferredSize(new Dimension(325, 30));

        textfield.getDocument().addDocumentListener(new DocumentListener() {
            void update() {
                switch(operation) {
                    case OPEN_ALL: {
                        File file = new File(textfield.getText());
                        okay.setEnabled(file.exists() && file.isAbsolute());
                        break;
                    } case OPEN_FILE: {
                        File file = new File(textfield.getText());
                        okay.setEnabled(file.exists() && file.isFile() && file.isAbsolute());
                        break;
                    } case OPEN_DIRECTORY: {
                        File file = new File(textfield.getText());
                        okay.setEnabled(file.exists() && file.isDirectory() && file.isAbsolute());
                        break;
                    } case SAVE: {
                        File file = new File(textfield.getText());
                        okay.setEnabled(file.isAbsolute());
                        break;
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                update();
            }
        });

        input.add(textfield, BorderLayout.WEST);

        textfield.setText(defaultPath);

        browse.setMargin(new Insets(5, 5, 5, 5));
        browse.setPreferredSize(new Dimension(75, 30));
        browse.setFocusPainted(false);

        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(new int[] { JFileChooser.FILES_AND_DIRECTORIES, JFileChooser.FILES_ONLY, JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_AND_DIRECTORIES }[operation]);
                jfc.setCurrentDirectory(new File(defaultPath));
                jfc.setDialogTitle((operation <= 2) ? "Open..." : "Save...");
                int result = jfc.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = jfc.getSelectedFile().getAbsolutePath();
                    textfield.setText(filePath);
                }
            }
        });

        input.add(browse, BorderLayout.EAST);

        dialog.add(input);

        okay.setFocusPainted(false);
        okay.setPreferredSize(new Dimension(75, 25));
        // okay.setEnabled(false);
        okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane pane = getOptionPane((JComponent) e.getSource());
                pane.setValue(okay);
            }
        });

        cancel.setFocusPainted(false);
        cancel.setPreferredSize(new Dimension(75, 25));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent) e.getSource());
                pane.setValue(cancel);
            }
        });

        int result = JOptionPane.showOptionDialog(Window.jframe, dialog, windowLabel, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { okay, cancel }, null);

        if (result == JFileChooser.APPROVE_OPTION) {
            value = textfield.getText();
        }

    }

    protected static JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane = null;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent) parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
}