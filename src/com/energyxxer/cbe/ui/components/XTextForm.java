package com.energyxxer.cbe.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class XTextForm extends XForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6491524519237599852L;

	protected JLabel label;
	public XTextField field;
	protected int labelWidth = 100;
	
	{
		this.setOpaque(false);
		this.label = new JLabel();
		this.field = new XTextField();
		this.label.setHorizontalTextPosition(SwingConstants.LEFT);
		
		this.add(this.label,BorderLayout.WEST);
		this.add(this.field,BorderLayout.CENTER);
	}
	
	public XTextForm(String label) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(100);
	}
	
	public XTextForm(String label, String value) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(100);
		setValue(value);
	}
	
	public XTextForm(String label, int labelWidth) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(labelWidth);
	}
	
	public XTextForm(String label, String value, int labelWidth) {
		super(new BorderLayout());
		setLabel(label);
		setLabelWidth(labelWidth);
		setValue(value);
	}
	
	public void setLabel(String text) {
		this.label.setText(text);
	}
	
	public void setLabelWidth(int width) {
		labelWidth = width;
		this.label.setPreferredSize(new Dimension(labelWidth,this.label.getPreferredSize().height));
	}

	public Object getValue() {
		return field.getText();
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
	
	public void setValue(String text) {
		field.setText(text);
	}
}
