package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;

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

		this.setOpaque(true);
		this.setBackground(new Color(0,0,0,0));

		{
			JPanel separatorLine = new JPanel(new BorderLayout());
			separatorLine.setPreferredSize(new Dimension(2, 20));
			//separatorLine.setBackground(CraftrWindow.theme.l2);
			this.add(separatorLine);

			JPanel lightLine = new JPanel();
			lightLine.setPreferredSize(new Dimension(1, 1));
			//lightLine.setBackground(CraftrWindow.theme.p2);
			separatorLine.add(lightLine, BorderLayout.EAST);

			ThemeChangeListener.addThemeChangeListener(t -> {
				lightLine.setBackground(t.getColor(new Color(235, 235, 235), "Toolbar.separator.light"));
				separatorLine.setBackground(t.getColor(new Color(150, 150, 150), "Toolbar.separator.dark"));
			});
		}
	}
}
