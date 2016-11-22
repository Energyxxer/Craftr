package com.energyxxer.cbe.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.energyxxer.cbe.main.Window;

/**
 * An explorer item. Can contain multiple explorer items.
 */
public class ExplorerItem extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2182029271616352961L;

	JPanel header;
	JPanel children;
	public String path;
	public ExplorerExpand expand;
	ExplorerItem parent;

	public ExplorerItem(File file, ExplorerItem parent, ArrayList<String> toOpen) {
		super(new BorderLayout());
		this.path = file.getAbsolutePath();
		this.parent = parent;

		header = new JPanel(new BorderLayout());
		header.setBackground(Window.theme.p1);

		this.add(header, BorderLayout.NORTH);

		expand = new ExplorerExpand(this);

		header.add(expand, BorderLayout.WEST);

		expand.setEnabled(file.isDirectory() && file.listFiles().length > 0);

		ExplorerItemLabel label = new ExplorerItemLabel(file, this);

		header.add(label, BorderLayout.CENTER);

		JPanel indentation = new JPanel();
		indentation.setPreferredSize(new Dimension(15, 1));
		indentation.setBackground(Window.theme.p1);
		this.add(indentation, BorderLayout.WEST);

		children = new JPanel();
		children.setBackground(Window.theme.p1);
		children.setLayout(new BoxLayout(children, BoxLayout.Y_AXIS));
		this.add(children, BorderLayout.CENTER);

		this.updateView();

		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		if(toOpen != null && toOpen.contains(this.path)) {
			expand(toOpen);
		}
	}

	public void expand() {
		expand(null);
	}

	public void expand(ArrayList<String> toOpen) {

		children.removeAll();
		Explorer.openDirectories.add(path);

		File[] childrenFiles = new File(this.path).listFiles();

		if (childrenFiles != null && childrenFiles.length > 0) {
			ArrayList<File> files = new ArrayList<File>();
			for (int i = 0; i < childrenFiles.length; i++) {

				if (childrenFiles[i].isDirectory()) {
					files.add(childrenFiles[i]);
				}
			}
			for (int i = 0; i < childrenFiles.length; i++) {

				if (childrenFiles[i].isFile() && (!childrenFiles[i].getName().equals(".project") || Explorer.SHOW_PROJECT_FILES)) {
					files.add(childrenFiles[i]);
				}
			}

			for (int i = 0; i < files.size(); i++) {
				children.add(new ExplorerItem(files.get(i), this, toOpen));
			}
		}

		this.updateNest();
	}

	public void collapse() {
		children.removeAll();
		Explorer.openDirectories.remove(path);

		this.updateNest();
	}

	public void updateNest() {

		this.updateView();
		if (this.parent != null) {
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