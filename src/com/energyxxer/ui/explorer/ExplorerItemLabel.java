package com.energyxxer.ui.explorer;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.energyxxer.cbe.Preferences;
import com.energyxxer.cbe.TabManager;
import com.energyxxer.cbe.Window;
import com.energyxxer.ui.common.MenuItems;
import com.energyxxer.ui.common.MenuItems.FileMenuItem;
import com.energyxxer.util.ImageManager;

public class ExplorerItemLabel extends JLabel implements MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655326885011257489L;
	
	public boolean rollover = false;
	public boolean selected = false;

	public static Color rollover_background = new Color(240,245,255);
	public static Color rollover_border = new Color(200,220,230);

	public static Color selected_background = new Color(200,220,230);
	public static Color selected_border = new Color(180,200,210);
	
	private ExplorerItem parent;
	
	public ExplorerItemLabel(File file,ExplorerItem parent) {
		super(file.getName());
		this.parent = parent;
		if(file.isDirectory()) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/folder.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			try {
				File workspaceFile = new File(Preferences.data.get("workspace_dir"));
				if(file.getParentFile().getCanonicalPath() == workspaceFile.getCanonicalPath()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/project.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				}
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(Window.consoleout));
			}
		} else {
			if(file.getName().endsWith(".mcbe")) {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/entity.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			} else if(file.getName().endsWith(".mcbi")) {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/item.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			} else {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/file.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			}
		}
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		
		if(selected) {
			g2.setColor(selected_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			GradientPaint grad = new GradientPaint(this.getWidth()/2,0,Color.WHITE, this.getWidth()/2, this.getHeight(), selected_background);
			
			g2.setPaint(grad);
			g2.fillRect(1,1,this.getWidth()-2,this.getHeight()-2);
			
			g2.setPaint(null);
			
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,this.getHeight()-2,2,2);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,2,2);
			
			g2.setColor(selected_border);
			g2.fillRect(1,1,1,1);
			g2.fillRect(1,this.getHeight()-2,1,1);
			g2.fillRect(this.getWidth()-2,1,1,1);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,1,1);
		} else if(rollover) {
			g2.setColor(rollover_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			g2.setColor(rollover_background);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,2,2);
			g2.fillRect(0,this.getHeight()-2,2,2);
			g2.fillRect(this.getWidth()-2,0,2,2);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,2,2);
			
			g2.setColor(rollover_border);
			g2.fillRect(1,1,1,1);
			g2.fillRect(1,this.getHeight()-2,1,1);
			g2.fillRect(this.getWidth()-2,1,1,1);
			g2.fillRect(this.getWidth()-2,this.getHeight()-2,1,1);
		}
		
		super.paintComponent(g);
	}
	
	public static void setNewSelected(ExplorerItemLabel newSelected) {
		if(Explorer.selectedLabel != null) {
			Explorer.selectedLabel.selected = false;
			Explorer.selectedLabel.repaint();
		}
		Explorer.selectedLabel = newSelected;
		if(newSelected != null) {
			newSelected.selected = true;
			newSelected.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		rollover = true;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		rollover = false;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		setNewSelected(this);
		if (e.getClickCount() % 2 == 0 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1) {
		     e.consume();
		     
		     File file = new File(parent.path);
		     if(file.isDirectory()) {
		    	 parent.expand.doClick();
		     } else {
		    	//byte[] encoded;
				//encoded = Files.readAllBytes(Paths.get(parent.path));
				//String s = new String(encoded);
				TabManager.openTab(parent.path);
				//CBEEditor.editor.setText(s);
		     }
		}
		if(e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setNewSelected(this);
		if(e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	private void showContextMenu(MouseEvent e) {
		ExplorerItemPopup menu = new ExplorerItemPopup();
        menu.show(e.getComponent(), e.getX(), e.getY());
	}
}

class ExplorerItemPopup extends JPopupMenu {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;
    public ExplorerItemPopup(){
    	add(MenuItems.newMenu("New                    "));
    	addSeparator();
    	add(new JMenuItem("Open"));
    	addSeparator();
    	add(MenuItems.fileItem(FileMenuItem.COPY));
    	add(MenuItems.fileItem(FileMenuItem.PASTE));
    	add(MenuItems.fileItem(FileMenuItem.DELETE));
    	addSeparator();
    	add(MenuItems.refactorMenu("Refactor"));
    }
}