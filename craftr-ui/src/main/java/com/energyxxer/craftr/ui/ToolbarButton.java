package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.util.Constant;
import com.energyxxer.util.ImageManager;
import com.energyxxer.xswing.hints.Hint;
import com.energyxxer.xswing.hints.TextHint;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a single button in the toolbar.
 */
public class ToolbarButton extends JButton implements MouseListener {

	private String icon = null;

	private String hintText = "";
	private Constant preferredPos = Hint.BELOW;

	public ToolbarButton(String icon, ThemeListenerManager tlm) {
		super();
		this.icon = icon;
		this.setContentAreaFilled(false);
		this.setOpaque(false);
		this.setBackground(new Color(0,0,0,0));

		this.setMinimumSize(new Dimension(25, 25));
		this.setMaximumSize(new Dimension(25, 25));
		this.setPreferredSize(new Dimension(25, 25));
		this.setBorder(BorderFactory.createEmptyBorder());

		tlm.addThemeChangeListener(t -> {
			if(icon != null) this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
		});

		this.setFocusPainted(false);

		this.addMouseListener(this);
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
		}

		g2.setComposite(previousComposite);

		super.paintComponent(g);
	}

	public String getHintText() {
		return hintText;
	}

	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	public Constant getPreferredPos() {
		return preferredPos;
	}

	public void setPreferredPos(Constant preferredPos) {
		this.preferredPos = preferredPos;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		TextHint hint = CraftrWindow.toolbar.hint;

		hint.setText(hintText);
		hint.setPreferredPos(this.preferredPos);
		Point point = this.getLocationOnScreen();
		point.x += this.getWidth()/2;
		point.y += this.getHeight()/2;
		hint.show(point, this.getModel()::isRollover);
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
