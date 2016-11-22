package com.energyxxer.cbe.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class XFileForm extends XForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5464206934373655681L;

	protected JLabel label;
	public XFileField field;
	protected int labelWidth = 100;
	
	{
		this.setOpaque(false);
		this.label = new JLabel();
		this.field = new XFileField();
		this.label.setHorizontalTextPosition(SwingConstants.LEFT);
		
		this.add(this.label,BorderLayout.WEST);
		this.add(this.field,BorderLayout.CENTER);
	}
	
	public XFileForm(String label) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(100);
	}
	
	public XFileForm(String label, int labelWidth) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(labelWidth);
	}
	
	public XFileForm(String label, File file) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(100);
		setValue(file);
	}
	
	public XFileForm(String label, File file, int labelWidth) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(labelWidth);
		setValue(file);
	}
	
	public void setLabel(String text) {
		this.label.setText(text);
	}
	
	public void setLabelWidth(int width) {
		labelWidth = width;
		this.label.setPreferredSize(new Dimension(labelWidth,this.label.getPreferredSize().height));
	}

	public Object getValue() {
		return field.getFile();
	}
	
	public void setForeground(Color fg) {
		if(label != null) label.setForeground(fg);
		super.setForeground(fg);
	}
	
	public void setFont(Font f) {
		if(label != null) label.setFont(f);
		if(field != null) field.setFont(f);
		super.setFont(f);
	}
	
	public void setValue(File file) {
		field.setFile(file);
	}

}
