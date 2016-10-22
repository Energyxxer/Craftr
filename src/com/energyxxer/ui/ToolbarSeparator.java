package com.energyxxer.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.energyxxer.cbe.Window;

/**
 * It's literally just a line.
 */
public class ToolbarSeparator extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278983722607671260L;

	public ToolbarSeparator() {
		super();
		this.setMinimumSize(new Dimension(15, 30));
		this.setMaximumSize(new Dimension(15, 30));
		this.setPreferredSize(new Dimension(15, 30));
		this.setBackground(Window.toolbarColor);

		{
			JPanel separatorLine = new JPanel(new BorderLayout());
			separatorLine.setPreferredSize(new Dimension(2, 20));
			separatorLine.setBackground(new Color(150, 160, 170));
			this.add(separatorLine);

			JPanel lightLine = new JPanel();
			lightLine.setPreferredSize(new Dimension(1, 1));
			separatorLine.add(lightLine, BorderLayout.EAST);
		}
	}
}
