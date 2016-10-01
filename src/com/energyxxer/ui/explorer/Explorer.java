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

/**
 * The component that shows a list of files in the workspace.
 * */
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
		
		File workspace = new File(Preferences.get("workspace_dir"));
		
		File[] fileList = workspace.listFiles();
		if(fileList == null) {
			revalidate();
			repaint();
			return;
		}

		ArrayList<File> files = new ArrayList<File>();
		
		for(int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if(file.isDirectory() && new File(file.getAbsolutePath() + File.separator + ".project").exists()) {
				files.add(file);
			}
		}
		for(int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if(file.isFile() && !file.getName().equals(".project")) {
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
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		ExplorerItemLabel.setNewSelected(null);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
