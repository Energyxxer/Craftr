package com.energyxxer.ui.explorer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.energyxxer.cbe.Preferences;

public class Explorer extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9100022225860900968L;

	public static ExplorerItemLabel selectedLabel = null;
	
	public Explorer() {
		super();
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBackground(Color.WHITE);
		
		this.addMouseListener(this);
	}
	
	public void generateProjectList() {
		
		removeAll();
		
		File workspace = new File(Preferences.data.get("workspace_dir"));
		
		File[] fileList = workspace.listFiles();

		ArrayList<File> files = new ArrayList<File>();
		
		for(int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if(file.isDirectory()) {
				files.add(file);
			}
		}
		for(int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if(file.isFile()) {
				files.add(file);
			}
		}
		Collections.reverse(files);
		
		for(int i = 0; i < files.size(); i++) {
			
			add(new ExplorerItem(files.get(i),null), FlowLayout.LEFT);
			
		}
		
		revalidate();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		ExplorerItemLabel.setNewSelected(null);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
