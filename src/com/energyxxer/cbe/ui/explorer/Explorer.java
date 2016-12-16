package com.energyxxer.cbe.ui.explorer;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The component that shows a list of files in the workspace.
 */
public class Explorer extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9100022225860900968L;
	
	static final boolean SHOW_PROJECT_FILES = true;

	public static ArrayList<ExplorerItemLabel> selectedLabels = new ArrayList<>();
	static ArrayList<String> openDirectories = new ArrayList<>();

	public Explorer() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ThemeChangeListener.addThemeChangeListener(t -> {
			setBackground(t.getColor("Explorer.background",Color.WHITE));
			generateProjectList();
		});


		this.addMouseListener(this);
	}

	public void generateProjectList() {
		ArrayList<String> copy = new ArrayList<>();
		copy.addAll(openDirectories);
		generateProjectList(copy);
	}

	private void generateProjectList(ArrayList<String> toOpen) {

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

		ArrayList<File> files = new ArrayList<>();

		for (File file : fileList) {
			if (file.isDirectory() && new File(file.getAbsolutePath() + File.separator + ".project").exists()) {
				files.add(file);
			}
		}
		for (File file : fileList) {
			if (file.isFile() && (!file.getName().equals(".project") || SHOW_PROJECT_FILES)) {
				files.add(file);
			}
		}
		Collections.reverse(files);

		for (File file : files) {

			add(new ExplorerItem(file, null, toOpen), FlowLayout.LEFT);

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
