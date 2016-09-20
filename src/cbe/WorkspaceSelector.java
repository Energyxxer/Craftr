package cbe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WorkspaceSelector {
	static String workspace_dir = "C:/Program Files/Command Block Engine/workspace";
	static boolean initialized = false;
	
	static JPanel dialog = new JPanel(new BorderLayout());
	static JPanel instructions = new JPanel();
	static JPanel input = new JPanel(new BorderLayout());
	static JTextField textfield = new JTextField(workspace_dir);
	static JButton browse = new JButton("Browse...");

	static JButton okay = new JButton("OK");
	static JButton cancel = new JButton("Cancel");
	
	public static void initialize() {
		instructions.setPreferredSize(new Dimension(400,50));
		
		instructions.add(new JLabel("<html>Specify the desired workspace directory.<br>This is where all your projects are going to be saved.</html>"),FlowLayout.LEFT);
		dialog.add(instructions,BorderLayout.NORTH);
		
		input.setPreferredSize(new Dimension(400,30));
		
		
		textfield.setPreferredSize(new Dimension(325,30));
		
		textfield.getDocument().addDocumentListener(new DocumentListener() {
            protected void update() {
            	File selected = new File(textfield.getText());
            	
            	boolean valid = true;
            	
            	try {
					selected.getCanonicalPath();
				} catch (IOException e) {
					valid = false;
				}
            	
                okay.setEnabled(selected != null && selected.isAbsolute() && valid);
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
		
		input.add(textfield,BorderLayout.WEST);

		browse.setMargin(new Insets(5,5,5,5));
		browse.setPreferredSize(new Dimension(75,30));
		browse.setFocusPainted(false);
		
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
			    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        jfc.setSelectedFile(new File(workspace_dir));
		        jfc.setDialogTitle("Set workspace location...");
		        int result = jfc.showSaveDialog(null);
		        if(result == JFileChooser.APPROVE_OPTION) {
		        	String filePath = jfc.getSelectedFile().getAbsolutePath();
		        	workspace_dir = filePath;
		        	textfield.setText(filePath);
		        }
			}
		});

		input.add(browse,BorderLayout.EAST);

		dialog.add(input);

		okay.setFocusPainted(false);
		okay.setPreferredSize(new Dimension(75,25));
		//okay.setEnabled(false);
		okay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(okay);
            }
        });
		
		cancel.setFocusPainted(false);
		cancel.setPreferredSize(new Dimension(75,25));
		cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = getOptionPane((JComponent)e.getSource());
                pane.setValue(cancel);
            }
        });
		
		initialized = true;
	}
	
	public static String prompt() {
		
		if(!initialized) {
			initialize();
		}
		
		int result = JOptionPane.showOptionDialog(Window.jframe, dialog, "Setup workspace", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] {okay, cancel}, null);
		
		System.out.println(dialog.getComponents());
		
		if(result == JFileChooser.APPROVE_OPTION) {
			return textfield.getText();
		}
		
		return null;
	}
	
	protected static JOptionPane getOptionPane(JComponent parent) {
        JOptionPane pane = null;
        if (!(parent instanceof JOptionPane)) {
            pane = getOptionPane((JComponent)parent.getParent());
        } else {
            pane = (JOptionPane) parent;
        }
        return pane;
    }
}
