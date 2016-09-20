package com.energyxxer.ui;

import com.energyxxer.cbe.Preferences;
import com.energyxxer.cbe.TabManager;
import com.energyxxer.util.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExplorerItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182029271616352961L;
	
	JPanel header;
	JPanel children;
	String path;
	ExplorerExpand expand;
	ExplorerItem parent;
	
	public ExplorerItem(File file, ExplorerItem parent) {
		super(new BorderLayout());
		this.path = file.getAbsolutePath();
		this.parent = parent;

		header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		
		this.add(header,BorderLayout.NORTH);

		
		expand = new ExplorerExpand(this);
		
		header.add(expand, BorderLayout.WEST);
		
		expand.setEnabled(file.isDirectory() && file.listFiles().length > 0);

		ExplorerItemLabel label = new ExplorerItemLabel(file,this);
		
		header.add(label,BorderLayout.CENTER);
		
		JPanel indentation = new JPanel();
		indentation.setPreferredSize(new Dimension(15,1));
		indentation.setBackground(Color.WHITE);
		this.add(indentation, BorderLayout.WEST);

		
		children = new JPanel();
		children.setBackground(Color.WHITE);
		children.setLayout(new BoxLayout(children,BoxLayout.Y_AXIS));
		this.add(children, BorderLayout.CENTER);
		
		this.updateView();
		
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	
	public void expand() {
		
		children.removeAll();
		
		File[] childrenFiles = new File(this.path).listFiles();
		
		
		if(childrenFiles != null && childrenFiles.length > 0) {
			ArrayList<File> files = new ArrayList<File>();
			for(int i = 0; i < childrenFiles.length; i++) {
				
				if(childrenFiles[i].isDirectory()) {
					files.add(childrenFiles[i]);
				}
			}
			for(int i = 0; i < childrenFiles.length; i++) {
				
				if(childrenFiles[i].isFile()) {
					files.add(childrenFiles[i]);
				}
			}
			
			for(int i = 0; i < files.size(); i++) {
				children.add(new ExplorerItem(files.get(i),this));
			}
		}

		this.updateNest();
	}
	
	public void collapse() {
		children.removeAll();
		
		this.updateNest();
	}
	
	public void updateNest() {

		this.updateView();
		if(this.parent != null) {
			this.parent.updateNest();
		}
	}
	
	public void updateView() {

		this.children.setMinimumSize(children.getPreferredSize());
		this.children.setMaximumSize(children.getPreferredSize());
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());
		
		this.revalidate();
		this.children.revalidate();

		this.repaint();
	}
}

class ExplorerItemLabel extends JLabel implements MouseListener, ActionListener {

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
	
	public static ExplorerItemLabel selectedLabel = null;
	
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
				e.printStackTrace();
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
		if(selectedLabel != null) {
			selectedLabel.selected = false;
			selectedLabel.repaint();
		}
		selectedLabel = newSelected;
		newSelected.selected = true;
		newSelected.repaint();
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
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}
