package com.energyxxer.cbe.ui.explorer;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.main.Window;

/**
 * The component that shows a list of files in the workspace.
 */
public class Explorer extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9100022225860900968L;
	
	public static final boolean SHOW_PROJECT_FILES = true;

	public static ArrayList<ExplorerItemLabel> selectedLabels = new ArrayList<ExplorerItemLabel>();
	public static ArrayList<String> openDirectories = new ArrayList<String>();

	public Explorer() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Window.theme.p1);

		this.addMouseListener(this);
	}

	public void generateProjectList() {
		ArrayList<String> copy = new ArrayList<String>();
		copy.addAll(openDirectories);
		generateProjectList(copy);
	}

	public void generateProjectList(ArrayList<String> toOpen) {

		ProjectManager.loadWorkspace();
		removeAll();

		openDirectories.clear();

		File workspace = new File(Preferences.get("workspace_dir"));

		File[] fileList = workspace.listFiles();
		if (fileList == null) {
			revalidate();
			repaint();
			return;
		}

		ArrayList<File> files = new ArrayList<File>();

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isDirectory() && new File(file.getAbsolutePath() + File.separator + ".project").exists()) {
				files.add(file);
			}
		}
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile() && (!file.getName().equals(".project") || SHOW_PROJECT_FILES)) {
				files.add(file);
			}
		}
		Collections.reverse(files);

		for (int i = 0; i < files.size(); i++) {

			add(new ExplorerItem(files.get(i), null, toOpen), FlowLayout.LEFT);

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
	public void mousePressed(MouseEvent e) {
		ExplorerItemLabel.setNewSelected(null, false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
