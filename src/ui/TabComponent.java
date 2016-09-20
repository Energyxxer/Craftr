package ui;

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
import javax.swing.SwingConstants;

import cbe.Tab;
import cbe.TabManager;
import cbe.Window;
import util.ImageManager;
import util.StringUtil;

public class TabComponent extends JLabel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118880845845349145L;
	
	public boolean selected = false;
	
	public String name;
	private boolean saved = true;

	public static Color rollover_background = Color.WHITE;
	public static Color rollover_border = new Color(150,150,150);

	public static Color selected_background = Color.WHITE;
	public static Color selected_border = new Color(180,200,210);
	
	TabCloseButton close;
	
	private Tab associatedTab;

	private boolean rollover;
	
	public TabComponent(Tab associatedTab) {
		super();
		this.associatedTab = associatedTab;
		setFont(new Font("Tahoma",0,11));

		setName(new File(this.getLinkedTab().path).getName());
		
		this.setLayout(new BorderLayout());
		
		close = new TabCloseButton(getPreferredSize().height,this);

		this.add(close,BorderLayout.EAST);
		
		setAlignmentX(FlowLayout.LEFT);
		setHorizontalAlignment(SwingConstants.LEFT);

		int gap = (30-this.getPreferredSize().height)/2;
		setBorder(BorderFactory.createEmptyBorder(gap+1,5,gap-1,5));
		
		addMouseListener(this);
		
		
		//setMaximumSize(new Dimension(getPreferredSize().width+5, 30));
	}
	
	public void setName(String name) {
		this.name = name;
		setToolTipText(name);
		
		if(name.endsWith(".mcbe")) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/entity.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		} else if(name.endsWith(".mcbi")) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/item.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		} else {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/file.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		}
		updateName();
	}
	
	public void updateName() {
		setText(((!saved) ? "*" : "") + StringUtil.ellipsis(name, 32));
		setPreferredSize(null);
		setPreferredSize(new Dimension(getPreferredSize().width+35, 30));
	}
	
	public Tab getLinkedTab() {
		return associatedTab;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
		updateName();
		this.revalidate();
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		if(selected) {
			g2.setColor(selected_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g2.setColor(selected_background);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-1);
			
			g2.setColor(Window.tabList.getBackground());
			g2.fillRect(0,0,1,4);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,0,4,1);
			g2.fillRect(this.getWidth()-4,0,4,1);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-1,0,1,4);
			
			g2.setColor(selected_border);
			g2.fillRect(1,2,1,2);
			g2.fillRect(2,1,2,1);
			g2.fillRect(this.getWidth()-2,2,1,2);
			g2.fillRect(this.getWidth()-4,1,2,1);
		} else if(rollover || close.getModel().isRollover()) {
			g2.setColor(rollover_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g2.setColor(rollover_background);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
			
			g2.setColor(Window.tabList.getBackground());
			g2.fillRect(0,0,1,4);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,0,4,1);
			g2.fillRect(this.getWidth()-4,0,4,1);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-1,0,1,4);
			
			g2.setColor(rollover_border);
			g2.fillRect(1,2,1,2);
			g2.fillRect(2,1,2,1);
			g2.fillRect(this.getWidth()-2,2,1,2);
			g2.fillRect(this.getWidth()-4,1,2,1);
		}
		
		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		rollover = true;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		rollover = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		TabManager.setSelectedTab(this.getLinkedTab());
	}
}

class TabCloseButton extends JButton implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4960575042835469824L;
	TabComponent parent;

	public TabCloseButton(int size, TabComponent parent) {
		super();
		this.parent = parent;
		setContentAreaFilled(false);
		setFocusPainted(false);
		setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close.png").getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH)));
		setToolTipText("Close");
		setPreferredSize(new Dimension(size,size));
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder());
		
		addActionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		parent.repaint();
		
		if(getModel().isEnabled()) {
			
			if(getModel().isRollover()) {
    			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close_hover.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
    		} else this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			
			if(/*parent.getModel().isRollover() ||*/ getModel().isRollover()) {
				setBackground(TabComponent.rollover_background);
			} else {
				setBackground(Window.tabList.getBackground());
			}
			
			super.paintComponent(g);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TabManager.closeTab(parent.getLinkedTab());
	}
}