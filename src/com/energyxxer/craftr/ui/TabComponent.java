package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.craftr.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.craftr.ui.theme.Theme;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.ImageManager;
import com.energyxxer.craftr.util.StringUtil;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import static com.energyxxer.craftr.ui.Draggable.AXIS_X;

/**
 * Representation of a tab in the interface.
 */
public class TabComponent extends JLabel implements MouseListener, ThemeChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118880845845349145L;

	/**Whether this tab component is the one selected and active.*/
	public boolean selected = false;

	private String name;
	private boolean saved = true;

	private TabCloseButton close;

	private Tab associatedTab;

	private boolean rollover;

	private Color normal_bg;
	private Color normal_line;
	private Color rollover_bg;
	private Color rollover_line;
	private Color selected_bg;
	private Color selected_line;

	private Draggable dragState = new Draggable(this, AXIS_X);

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

		dragState.addDragListener(new DragListener() {
			@Override
			public void onDrag(Point offset) {
				TabComponent.this.repaint();
			}

			@Override
			public void onDrop(Point point) {
				TabComponent.this.repaint();
			}
		});
	}

	@Override
	public void setName(String name) {
		this.name = name;
		setToolTipText(getLinkedTab().path);

		updateIcon();
		updateName();
	}

	private void updateIcon() {

		if(associatedTab.path.endsWith(".png")) {
			try {
				Image icon = ImageIO.read(new File(associatedTab.path));
				this.setIcon(new ImageIcon(icon.getScaledInstance(16,16, Image.SCALE_SMOOTH)));
			} catch(IOException x) {
				this.setIcon(new ImageIcon(Commons.getIcon("file").getScaledInstance(16,16, Image.SCALE_SMOOTH)));
			}
			return;
		}

		String icon = ProjectManager.getIconFor(new File(associatedTab.path));
		if(icon != null) {
			this.setIcon(new ImageIcon(Commons.getIcon(icon).getScaledInstance(16,16, Image.SCALE_SMOOTH)));
		} else if (name.endsWith(".craftr")) {
			this.setIcon(new ImageIcon(Commons.getIcon("entity").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(Commons.getIcon("file").getScaledInstance(16, 16,
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
		setPreferredSize(new Dimension(getPreferredSize().width + 30, 30));
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
		Point offset = dragState.getOffset();
		g2.translate(offset.x, offset.y);
		//Rectangle tabListRect = CraftrWindow.editArea.tabList.getBounds();
		//g2.setClip(new Rectangle(0,0,tabListRect.width,tabListRect.height));
		//System.out.println(getParent().getGraphics());

		if (selected) {
			g2.setColor(selected_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(selected_bg);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight());

		} else if (rollover || close.getModel().isRollover()) {
			g2.setColor(rollover_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(rollover_bg);
			g2.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		} else {
			g2.setColor(normal_line);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(normal_bg);
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
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			TabManager.setSelectedTab(this.getLinkedTab());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
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
		setFont(new Font(t.getString("Tab.font","General.font","default:Tahoma"), 0, 11));
		setForeground(t.getColor(Color.BLACK, "Tab.foreground","General.foreground"));

		this.normal_bg = t.getColor(new Color(200, 202, 205), "Tab.background");
		this.normal_line = t.getColor(new Color(200, 200, 200), "Tab.border");
		this.rollover_bg = t.getColor(Color.WHITE, "Tab.hover.background");
		this.rollover_line = t.getColor(new Color(200, 200, 200), "Tab.hover.border");
		this.selected_bg = t.getColor(Color.WHITE, "Tab.selected.background");
		this.selected_line = t.getColor(new Color(200, 200, 200), "Tab.selected.border");

		updateIcon();
		updateName();
	}
}

class TabPopup extends StyledPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;

	TabPopup(Tab tab) {
		{
			StyledMenuItem item = new StyledMenuItem("Close");
			item.addActionListener(e -> TabManager.closeTab(tab));
			add(item);
		}
		{
			StyledMenuItem item = new StyledMenuItem("Close Others");
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
			StyledMenuItem item = new StyledMenuItem("Close Tabs to the Left");
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
			StyledMenuItem item = new StyledMenuItem("Close Tabs to the Right");
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
			StyledMenuItem item = new StyledMenuItem("Close All");
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
	public void mouseClicked(MouseEvent arg0) {}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		parent.mouseEntered(arg0);
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		parent.mouseExited(arg0);
	}


	@Override
	public void mousePressed(MouseEvent arg0) {}


	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	
}