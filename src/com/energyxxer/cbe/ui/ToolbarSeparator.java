package com.energyxxer.cbe.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.energyxxer.cbe.main.Window;

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
		this.setBackground(Window.theme.p2);

		{
			JPanel separatorLine = new JPanel(new BorderLayout());
			separatorLine.setPreferredSize(new Dimension(2, 20));
			separatorLine.setBackground(Window.theme.l1);
			this.add(separatorLine);

			JPanel lightLine = new JPanel();
			lightLine.setPreferredSize(new Dimension(1, 1));
			lightLine.setBackground(Window.theme.p1);
			separatorLine.add(lightLine, BorderLayout.EAST);
		}
	}
}
