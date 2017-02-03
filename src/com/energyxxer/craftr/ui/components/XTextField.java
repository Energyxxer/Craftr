package com.energyxxer.craftr.ui.components;

import javax.swing.*;
import java.awt.*;

public class XTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5119684159251378123L;
	
	{
		setBackground(new Color(225,225,225));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(150,150,150)), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
	}
	
	public XTextField() {
		super();
	}
	
	public XTextField(String text) {
		super(text);
	}
	
	public XTextField(int columns) {
		super(columns);
	}
	
	public XTextField(String text, int columns) {
		super(text, columns);
	}
	
	public void setBorderColor(Color bc) {
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(bc), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
	}
	
	public void setForeground(Color fg) {
		this.setCaretColor(fg);
		super.setForeground(fg);
	}
	
}
