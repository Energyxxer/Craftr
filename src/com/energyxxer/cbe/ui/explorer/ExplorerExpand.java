package com.energyxxer.cbe.ui.explorer;

import com.energyxxer.cbe.util.ImageManager;

import javax.swing.*;
import java.awt.*;

/**
 * Little button next to explorer items that can be expanded.
 */
public class ExplorerExpand extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107525878261161344L;

	boolean expanded = false;
	private ExplorerItem parent = null;

	@Override
	protected void paintComponent(Graphics g) {

		if (getModel().isEnabled()) {
			super.paintComponent(g);

			if (expanded) {
				if (getModel().isRollover()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/collapse_hover.png")
							.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				} else
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/collapse.png").getScaledInstance(16,
							16, java.awt.Image.SCALE_SMOOTH)));
			} else {
				if (getModel().isRollover()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/expand_hover.png")
							.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				} else
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/expand.png").getScaledInstance(16,
							16, java.awt.Image.SCALE_SMOOTH)));
			}
		}
	}

	ExplorerExpand(ExplorerItem parent) {
		super();
		this.parent = parent;
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setPreferredSize(new Dimension(25, 25));
		this.setBorder(BorderFactory.createEmptyBorder());

		this.addActionListener(e -> {
			ExplorerExpand source = ((ExplorerExpand) e.getSource());
			source.expanded = !source.expanded;
			if (source.expanded) {
				source.parent.expand();
			} else {
				source.parent.collapse();
			}
		});
	}
}
