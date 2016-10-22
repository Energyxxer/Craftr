package com.energyxxer.ui.explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.energyxxer.util.ImageManager;

/**
 * Little button next to explorer items that can be expanded.
 */
public class ExplorerExpand extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107525878261161344L;

	public boolean expanded = false;
	ExplorerItem parent = null;

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

	public ExplorerExpand(ExplorerItem parent) {
		super();
		this.parent = parent;
		this.setBackground(Color.BLACK);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setPreferredSize(new Dimension(25, 25));
		this.setBorder(BorderFactory.createEmptyBorder());

		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ExplorerExpand source = ((ExplorerExpand) e.getSource());
				source.expanded = !source.expanded;
				if (source.expanded) {
					source.parent.expand();
				} else {
					source.parent.collapse();
				}
			}

		});
	}
}
