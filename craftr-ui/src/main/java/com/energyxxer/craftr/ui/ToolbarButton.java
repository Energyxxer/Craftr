package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.theme.change.ThemeListenerManager;
import com.energyxxer.util.Constant;
import com.energyxxer.xswing.hints.Hint;
import com.energyxxer.xswing.hints.TextHint;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import java.awt.event.MouseMotionListener;

/**
 * Represents a single button in the toolbar.
 */
public class ToolbarButton extends JButton implements MouseListener, MouseMotionListener {

    private static final int MARGIN = 1;
    private static final int BORDER_THICKNESS = 1;
    public static final int SIZE = 25;

    private Color background = Color.GRAY;
    private Color rolloverBackground = Color.GRAY;
    private Color pressedBackground = Color.GRAY;
    private Color border = Color.BLACK;
    private Color rolloverBorder = Color.BLACK;
    private Color pressedBorder = Color.BLACK;

	private String hintText = "";
	private Constant preferredPos = Hint.BELOW;

	private boolean rollover = false;

	public ToolbarButton(String icon, ThemeListenerManager tlm) {
		this.setContentAreaFilled(false);
		this.setOpaque(false);
		this.setBackground(new Color(0,0,0,0));

		this.setMinimumSize(new Dimension(25, 25));
		this.setMaximumSize(new Dimension(25, 25));
		this.setPreferredSize(new Dimension(25, 25));
		this.setBorder(BorderFactory.createEmptyBorder());

		tlm.addThemeChangeListener(t -> {
			if(icon != null) this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
            this.background = t.getColor(Color.GRAY, "Toolbar.button.background", "General.button.background");
            this.rolloverBackground = t.getColor(Color.GRAY, "Toolbar.button.hover.background", "General.button.hover.background", "Toolbar.button.background", "General.button.background");
            this.pressedBackground = t.getColor(Color.GRAY, "Toolbar.button.pressed.background", "General.button.pressed.background", "Toolbar.button.hover.background", "General.button.hover.background", "Toolbar.button.background", "General.button.background");
            this.border = t.getColor(Color.BLACK, "Toolbar.button.border.color", "General.button.border.color");
            this.rolloverBorder = t.getColor(Color.BLACK, "Toolbar.button.hover.border.color", "General.button.hover.border.color", "Toolbar.button.border.color", "General.button.border.color");
            this.pressedBorder = t.getColor(Color.BLACK, "Toolbar.button.pressed.border.color", "General.button.pressed.border.color", "Toolbar.button.hover.border.color", "General.button.hover.border.color", "Toolbar.button.border.color", "General.button.border.color");
        });

		this.setFocusPainted(false);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Composite previousComposite = g2.getComposite();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getModel().isPressed() ? pressedBackground : getModel().isRollover() ? rolloverBackground : background);
        g.fillRect(MARGIN+BORDER_THICKNESS,MARGIN+BORDER_THICKNESS,SIZE-2*MARGIN-2*BORDER_THICKNESS,SIZE-2*MARGIN-2*BORDER_THICKNESS);
        g.setColor(getModel().isPressed() ? pressedBorder : getModel().isRollover() ? rolloverBorder : border);
        g.fillRect(MARGIN,MARGIN,SIZE-2*MARGIN-BORDER_THICKNESS,BORDER_THICKNESS);
        g.fillRect(SIZE-MARGIN-BORDER_THICKNESS,MARGIN,BORDER_THICKNESS,SIZE-2*MARGIN-BORDER_THICKNESS);
        g.fillRect(MARGIN+BORDER_THICKNESS,SIZE-MARGIN-BORDER_THICKNESS,SIZE-2*MARGIN-BORDER_THICKNESS,BORDER_THICKNESS);
        g.fillRect(MARGIN,MARGIN+BORDER_THICKNESS,BORDER_THICKNESS,SIZE-2*MARGIN-BORDER_THICKNESS);

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
		rollover = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		rollover = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		rollover = true;
		TextHint hint = CraftrWindow.toolbar.hint;
		if(!hint.isShowing()) {
			hint.setText(hintText);
			hint.setPreferredPos(this.preferredPos);
			Point point = this.getLocationOnScreen();
			point.x += this.getWidth()/2;
			point.y += this.getHeight()/2;
			hint.show(point, () -> rollover);
		}
	}
}
