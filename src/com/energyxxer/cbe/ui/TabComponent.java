package com.energyxxer.cbe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.components.themechange.TCMenuItem;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.StringUtil;

/**
 * Representation of a tab in the interface.
 */
public class TabComponent extends JLabel implements MouseListener, ThemeChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118880845845349145L;

	//Whether this tab component is the one selected and active.
	public boolean selected = false;

	private String name;
	private boolean saved = true;

	private TabCloseButton close;

	private Tab associatedTab;

	private boolean rollover;


	TabComponent(Tab associatedTab) {
		super();
		this.associatedTab = associatedTab;

		setName(new File(this.getLinkedTab().path).getName());

		setLayout(new BorderLayout());

		close = new TabCloseButton(getPreferredSize().height, this);

		add(close, BorderLayout.EAST);

		setAlignmentX(FlowLayout.LEFT);
		setHorizontalAlignment(SwingConstants.LEFT);

		int gap = (30 - this.getPreferredSize().height) / 2;
		setBorder(BorderFactory.createEmptyBorder(gap + 1, 5, gap - 1, 5));

		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		
		addMouseListener(this);
		addThemeChangeListener();
	}

	@Override
	public void setName(String name) {
		this.name = name;
		setToolTipText(getLinkedTab().path);

		updateIcon();
		updateName();
	}

	private void updateIcon() {
		if (name.endsWith(".mcbe")) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "entity.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "file.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	private void updateName() {
		setText(((!saved) ? "*" : "") + StringUtil.ellipsis(name, 32));
		setPreferredSize(null);
		setPreferredSize(new Dimension(getPreferredSize().width + 25, 30));
		revalidate();
	}

	public Tab getLinkedTab() {
		return associatedTab;
	}

	void setSaved(boolean saved) {
		this.saved = saved;
		updateName();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void repaint() {
		super.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;

		if (selected) {
			g2.setColor(Window.theme.l1);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(Window.theme.p1);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight());

		} else if (rollover || close.getModel().isRollover()) {
			g2.setColor(Window.theme.l1);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(Window.theme.p1);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		} else {
			g2.setColor(Window.theme.l1);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(Window.tabList.getBackground());
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		}

		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		rollover = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		rollover = false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			TabManager.setSelectedTab(this.getLinkedTab());
		}
	}

	private void showContextMenu(MouseEvent e) {
		TabPopup menu = new TabPopup(getLinkedTab());
		menu.show(this, e.getX(), e.getY());
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void themeChanged(Theme t) {
		setFont(new Font(t.font1, 0, 11));
		setForeground(t.t1);
		if (name.endsWith(".mcbe")) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + t.path + "entity.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + t.path + "file.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		}
	}
}

class TabPopup extends JPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;

	TabPopup(Tab tab) {
		{
			TCMenuItem item = new TCMenuItem("Close");
			item.addActionListener(e -> TabManager.closeTab(tab));
			add(item);
		}
		{
			TCMenuItem item = new TCMenuItem("Close Others");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					if (TabManager.openTabs.get(i) != tab) {
						TabManager.closeTab(TabManager.openTabs.get(i));
					} else {
						i++;
					}
				}
			});
			add(item);
		}
		{
			TCMenuItem item = new TCMenuItem("Close Tabs to the Left");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					if (TabManager.openTabs.get(i) == tab) {
						return;
					} else {
						TabManager.closeTab(TabManager.openTabs.get(i));
					}
				}
			});
			add(item);
		}

		{
			TCMenuItem item = new TCMenuItem("Close Tabs to the Right");
			item.addActionListener(e -> {
				boolean close = false;
				for (int i = 0; i < TabManager.openTabs.size(); i++) {
					if (TabManager.openTabs.get(i) == tab) {
						close = true;
					} else if (close) {
						TabManager.closeTab(TabManager.openTabs.get(i));
						i--;
					}
				}
			});
			add(item);
		}

		addSeparator();
		{
			TCMenuItem item = new TCMenuItem("Close All");
			item.addActionListener(e -> {
				for (int i = 0; i < TabManager.openTabs.size();) {
					TabManager.closeTab(TabManager.openTabs.get(i));
				}
			});
			add(item);
		}
	}
}

class TabCloseButton extends JButton implements ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4960575042835469824L;
	private TabComponent parent;

	TabCloseButton(int size, TabComponent parent) {
		super();
		this.parent = parent;
		setContentAreaFilled(false);
		setFocusPainted(false);
		setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		setRolloverIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close_hover.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		setPressedIcon(getRolloverIcon());
		setToolTipText("Close");
		setPreferredSize(new Dimension(size, size));
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		setBorder(BorderFactory.createEmptyBorder());

		addActionListener(this);
		addMouseListener(this);
	}
	

	@Override
	protected void paintComponent(Graphics g) {

		if (getModel().isEnabled()) {
			setBackground(new Color(0,0,0,0));

			super.paintComponent(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TabManager.closeTab(parent.getLinkedTab());
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		parent.mouseEntered(arg0);
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		parent.mouseExited(arg0);
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}