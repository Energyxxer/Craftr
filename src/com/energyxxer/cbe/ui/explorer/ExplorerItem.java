package com.energyxxer.cbe.ui.explorer;

import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * An explorer item. Can contain multiple explorer items.
 */
public class ExplorerItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182029271616352961L;

	private JPanel header;
	private JPanel children;
	public String path;
	ExplorerExpand expand;
	ExplorerItem parent;

	ExplorerItem(File file, ExplorerItem parent, ArrayList<String> toOpen) {
		super(new BorderLayout());
		this.path = file.getAbsolutePath();
		this.parent = parent;

		header = new JPanel(new BorderLayout());


		this.add(header, BorderLayout.NORTH);

		expand = new ExplorerExpand(this);

		header.add(expand, BorderLayout.WEST);

		File[] fileList = file.listFiles();
		expand.setEnabled(file.isDirectory() && fileList != null && fileList.length > 0);

		ExplorerItemLabel label = new ExplorerItemLabel(file, this);

		header.add(label, BorderLayout.CENTER);

		JPanel indentation = new JPanel();
		indentation.setPreferredSize(new Dimension(15, 1));
		this.add(indentation, BorderLayout.WEST);

		children = new JPanel();
		children.setLayout(new BoxLayout(children, BoxLayout.Y_AXIS));
		this.add(children, BorderLayout.CENTER);

		this.updateView();

		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		ThemeChangeListener.addThemeChangeListener(t -> {
			header.setBackground(t.getColor("Explorer.background",Color.WHITE));
			indentation.setBackground(header.getBackground());
			children.setBackground(header.getBackground());
		});

		if(toOpen != null && toOpen.contains(this.path)) {
			expand(toOpen);
			expand.expanded = true;
		}
	}

	void expand() {
		expand(null);
	}

	private void expand(ArrayList<String> toOpen) {

		children.removeAll();
		Explorer.openDirectories.add(path);

		File[] childrenFiles = new File(this.path).listFiles();

		if (childrenFiles != null && childrenFiles.length > 0) {
			ArrayList<File> files = new ArrayList<>();
			for (File child : childrenFiles) if (child.isDirectory()) files.add(child);

			for (File child : childrenFiles)
				if (child.isFile() && (!child.getName().equals(".project") || Explorer.SHOW_PROJECT_FILES))
					files.add(child);

			for (File file : files) children.add(new ExplorerItem(file, this, toOpen));
		}

		this.updateNest();
	}

	void collapse() {
		children.removeAll();
		Explorer.openDirectories.remove(path);

		this.updateNest();
	}

	private void updateNest() {

		this.updateView();
		if (this.parent != null) {
			this.parent.updateNest();
		}
	}

	private void updateView() {

		this.children.setMinimumSize(children.getPreferredSize());
		this.children.setMaximumSize(children.getPreferredSize());
		this.setMinimumSize(this.getPreferredSize());
		this.setMaximumSize(this.getPreferredSize());

		this.revalidate();
		this.children.revalidate();

		this.repaint();
	}
}