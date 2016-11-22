package com.energyxxer.cbe.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;

public class XFileField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2485998770143980714L;
	
	protected XTextField field;
	protected XButton browse;
	
	{
		setOpaque(false);
		field = new XTextField();
		browse = new XButton("Browse...");
		browse.setPreferredSize(new Dimension(100,25));
		this.add(field, BorderLayout.CENTER);
		this.add(browse, BorderLayout.EAST);
	}
	
	public XFileField() {
		super(new BorderLayout());
	}
	
	public XFileField(File file) {
		super(new BorderLayout());
	}
	
	public void setFile(File file) {
		field.setText(file.getAbsolutePath());
	}
	
	public File getFile() {
		return new File(field.getText());
	}
	
	public void setBorderColor(Color bc) {
		field.setBorderColor(bc);
	}
}
