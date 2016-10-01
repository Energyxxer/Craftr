package com.energyxxer.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.energyxxer.cbe.Window;
import com.energyxxer.util.ImageManager;

/**
 * Represents a single button in the toolbar.
 * */
public class ToolbarButton extends JButton {

	public static Color hoverBackgroundColor = new Color(255,255,255,128);
	public static Color pressedBackgroundColor = Window.toolbarColor.brighter().brighter();

	/**
	 * 
	 */
	private static final long serialVersionUID = -3354785558861039404L;
	
	public ToolbarButton() {
		super();
		this.setContentAreaFilled(false);
		this.setBackground(Window.toolbarColor);
		this.setMinimumSize(new Dimension(25,25));
		this.setMaximumSize(new Dimension(25,25));
		this.setPreferredSize(new Dimension(25,25));
		this.setBorder(BorderFactory.createEmptyBorder());
		
		this.setFocusPainted(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	
    	Composite previousComposite = g2.getComposite();
    	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {

    		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f);
    		g2.setComposite(composite);
        	
            g2.drawImage(ImageManager.load("/assets/ui/button.png"), 0, 0, this.getWidth(), this.getHeight(), null);
        } else if (getModel().isRollover()) {

    		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    		g2.setComposite(composite);
    		
            g2.drawImage(ImageManager.load("/assets/ui/button.png"), 0, 0, this.getWidth(), this.getHeight(), null);
        } else {
        	//this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        }
        
        g2.setComposite(previousComposite);
        
        super.paintComponent(g);
    }
}
