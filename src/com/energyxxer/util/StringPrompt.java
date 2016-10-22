package com.energyxxer.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.energyxxer.cbe.Window;

/**
 * Asks the player to input a string. Has support for validation.
 */
public class StringPrompt {
	static JPanel dialog = new JPanel(new BorderLayout());
	static JPanel instructions = new JPanel();
	static JPanel input = new JPanel(new BorderLayout());
	static JTextField textfield = new JTextField("");

	static StringValidator validator = new StringValidator();

	static JLabel label = new JLabel(
			"<html>Specify the desired workspace directory.<br>This is where all your projects are going to be saved.</html>");

	static JButton okay = new JButton("OK");
	static JButton cancel = new JButton("Cancel");

	static {
		instructions.add(label, FlowLayout.LEFT);
		dialog.add(instructions, BorderLayout.NORTH);

		input.setPreferredSize(new Dimension(400, 25));

		textfield.setPreferredSize(new Dimension(400, 25));

		textfield.getDocument().addDocumentListener(new DocumentListener() {
			protected void update() {

				boolean valid = validator.validate(textfield.getText());

				okay.setEnabled(valid);
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

		textfield.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					okay.getActionListeners()[0].actionPerformed(new ActionEvent(okay, 0, null));
				}
			}
		});

		input.add(textfield, BorderLayout.WEST);

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
	}

	public static String prompt(String title, String prompt, String defaultText) {
		return prompt(title, prompt, defaultText, new StringValidator());
	}

	public static String prompt(String title, String prompt, String defaultText, StringValidator v) {
		validator = v;
		label.setText(prompt);
		textfield.setText(defaultText);
		int result = JOptionPane.showOptionDialog(Window.jframe, dialog, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { okay, cancel }, null);

		if (result == JFileChooser.APPROVE_OPTION) {
			return textfield.getText();
		}

		return null;
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
