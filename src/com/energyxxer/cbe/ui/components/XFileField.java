package com.energyxxer.cbe.ui.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class XFileField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2485998770143980714L;
	
	protected XTextField field;
	protected XButton browse;
	protected byte operation = OPEN_ALL;
	protected File value = new File(System.getProperty("user.home"));
	protected String dialogTitle = "Open...";

	public static final byte OPEN_ALL = 0;
	public static final byte OPEN_FILE = 1;
	public static final byte OPEN_DIRECTORY = 2;
	public static final byte SAVE = 3;
	
	{
		setOpaque(false);
		field = new XTextField();
		browse = new XButton("Browse...");
		browse.setPreferredSize(new Dimension(100,25));

		browse.addActionListener(e -> {
			JFileChooser jfc = new JFileChooser();
			int[] modes = new int[] {JFileChooser.FILES_AND_DIRECTORIES,JFileChooser.FILES_ONLY,JFileChooser.DIRECTORIES_ONLY,JFileChooser.FILES_AND_DIRECTORIES};
			jfc.setFileSelectionMode(modes[operation]);
			jfc.setCurrentDirectory(value);
			jfc.setDialogTitle(dialogTitle);
			int result = jfc.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				value = jfc.getSelectedFile();
				field.setText(value.getAbsolutePath());
			}
		});

		field.getDocument().addDocumentListener(new DocumentListener() {

			protected void update() {
				switch(operation) {
					case OPEN_ALL: {
						File file = new File(field.getText());
						if(file.exists()) XFileField.this.value = file;
						break;
					} case OPEN_FILE: {
						File file = new File(field.getText());
						if(file.exists() && file.isFile()) XFileField.this.value = file;
						break;
					} case OPEN_DIRECTORY: {
						File file = new File(field.getText());
						if(file.exists() && file.isDirectory()) XFileField.this.value = file;
						break;
					} case SAVE: {
						File file = new File(field.getText());
						XFileField.this.value = file;
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

		this.add(field, BorderLayout.CENTER);
		this.add(browse, BorderLayout.EAST);
	}

	public XFileField() {
		super(new BorderLayout());
	}

	public XFileField(byte operation) {
		super(new BorderLayout());
		setOperation(operation);
	}

	public XFileField(File file) {
		super(new BorderLayout());
		setFile(file);
	}

	public XFileField(byte operation, File file) {
		super(new BorderLayout());
		setFile(file);
		setOperation(operation);
	}

	public void setOperation(byte operation) {
		this.operation = operation;
	}
	
	public void setFile(File file) {
		if(file != null) {
			field.setText(file.getAbsolutePath());
			this.value = file;
		} else {
			File dFile = new File(System.getProperty("user.home"));
			field.setText(dFile.getAbsolutePath());
			this.value = dFile;
		}
	}
	
	public File getFile() {
		return new File(field.getText());
	}
	
	public void setBorderColor(Color bc) {
		field.setBorderColor(bc);
	}

	public void setDialogTitle(String title) {
		this.dialogTitle = title;
	}
}
