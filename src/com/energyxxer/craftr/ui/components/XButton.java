package com.energyxxer.craftr.ui.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class XButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6056286045892985732L;
	
	protected Color borderColor = new Color(150,150,150);
	protected Color rolloverColor = new Color(240,240,240);
	protected Color pressedColor = new Color(175,175,175);
	
	{
		setFocusPainted(false);
		setOpaque(false);
		setContentAreaFilled(false);
		setBackground(new Color(225,225,225));
		this.setBorderPainted(false);
	}
	
	public XButton() {
		super();
	}
	
	public XButton(String label) {
		super(label);
	}
	
	public XButton(String label, ImageIcon icon) {
		super(label, icon);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(this.getBorderColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(this.getModel().isPressed()) {
			g.setColor(this.getPressedColor());
		} else if(this.getModel().isRollover()) {
			g.setColor(this.getRolloverColor());
		} else {
			g.setColor(this.getBackground());
		}
		g.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
		super.paintComponent(g);
	}
	
	public void setBorderColor(Color c) {
		if(c != null) borderColor = c;
	}
	
	public Color getBorderColor() {
		return borderColor;
	}

	public Color getRolloverColor() {
		return rolloverColor;
	}

	public Color getPressedColor() {
		return pressedColor;
	}

	public void setRolloverColor(Color c) {
		this.rolloverColor = c;
	}

	public void setPressedColor(Color c) {
		this.pressedColor = c;
	}
}
