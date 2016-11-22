package com.energyxxer.cbe.ui.explorer;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.common.MenuItems;
import com.energyxxer.cbe.ui.common.MenuItems.FileMenuItem;
import com.energyxxer.cbe.util.ColorUtil;
import com.energyxxer.cbe.util.FileUtil;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.StringPrompt;
import com.energyxxer.cbe.util.StringUtil;
import com.energyxxer.cbe.util.StringValidator;

/**
 * Non-recursive label for an explorer item.
 */
public class ExplorerItemLabel extends JLabel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655326885011257489L;

	public boolean rollover = false;
	public boolean selected = false;

	public ExplorerItem parent;

	public ExplorerItemLabel(File file, ExplorerItem parent) {
		super(file.getName());
		this.parent = parent;
		this.setForeground(Window.theme.t1);
		updateLabel();
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
	}

	public void updateLabel() {
		File file = new File(parent.path);
		setText(file.getName());
		if (file.isDirectory()) {
			this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "folder.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			try {
				File workspaceFile = new File(Preferences.get("workspace_dir"));
				if (file.getParentFile().getCanonicalPath() == workspaceFile.getCanonicalPath()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "project.png").getScaledInstance(16, 16,
							java.awt.Image.SCALE_SMOOTH)));
				}
			} catch (IOException e) {
				e.printStackTrace(new PrintWriter(Window.consoleOut));
			}
		} else {
			if (file.getName().endsWith(".mcbe")) {
				String icon = ProjectManager.getIconFor(file);
				if(icon == null) icon = Window.theme.path + "warn";
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + icon + ".png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
			} else {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "file.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
			}
		}
		revalidate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (selected) {
			g2.setColor(Window.theme.b4);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			GradientPaint grad = new GradientPaint(this.getWidth() / 2, 0, ColorUtil.add(Window.theme.b3,Window.theme.g1), this.getWidth() / 2,
					this.getHeight(), Window.theme.b3);

			g2.setPaint(grad);
			g2.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);

			g2.setPaint(null);

			g2.setColor(Window.theme.p1);
			g2.fillRect(0, 0, 2, 2);
			g2.fillRect(0, this.getHeight() - 2, 2, 2);
			g2.fillRect(this.getWidth() - 2, 0, 2, 2);
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 2, 2);

			g2.setColor(Window.theme.b4);
			g2.fillRect(1, 1, 1, 1);
			g2.fillRect(1, this.getHeight() - 2, 1, 1);
			g2.fillRect(this.getWidth() - 2, 1, 1, 1);
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 1, 1);
		} else if (rollover) {
			g2.setColor(Window.theme.b2);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			g2.setColor(Window.theme.b1);
			g2.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
			g2.setColor(Window.theme.p1);
			g2.fillRect(0, 0, 2, 2);
			g2.fillRect(0, this.getHeight() - 2, 2, 2);
			g2.fillRect(this.getWidth() - 2, 0, 2, 2);
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 2, 2);

			g2.setColor(Window.theme.b2);
			g2.fillRect(1, 1, 1, 1);
			g2.fillRect(1, this.getHeight() - 2, 1, 1);
			g2.fillRect(this.getWidth() - 2, 1, 1, 1);
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 1, 1);
		}

		super.paintComponent(g);
	}

	public static void setNewSelected(ExplorerItemLabel newSelected, boolean ctrl) {
		if(!ctrl) {

			for(int i = 0; i < Explorer.selectedLabels.size();) {
				Explorer.selectedLabels.get(i).selected = false;
				Explorer.selectedLabels.get(i).repaint();
				Explorer.selectedLabels.remove(i);
			}
			if (newSelected != null) {
				newSelected.selected = true;
				newSelected.repaint();
				Explorer.selectedLabels.add(newSelected);
			}
		} else {
			for(int i = 0; i < Explorer.selectedLabels.size(); i++) {
				if(Explorer.selectedLabels.get(i) == newSelected) {
					Explorer.selectedLabels.get(i).selected = false;
					Explorer.selectedLabels.get(i).repaint();
					Explorer.selectedLabels.remove(i);
					return;
				}
			}
			if (newSelected != null) {
				newSelected.selected = true;
				newSelected.repaint();
				Explorer.selectedLabels.add(newSelected);
			}
		}
	}

	public void open() {
		File file = new File(parent.path);
		if (file.isDirectory()) {
			parent.expand.doClick();
		} else {
			TabManager.openTab(parent.path);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		rollover = true;
		this.repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		rollover = false;
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 || (e.getButton() == MouseEvent.BUTTON3 && !e.isControlDown() && Explorer.selectedLabels.size() <= 1)) {
			setNewSelected(this, e.isControlDown());
		}
		if (e.getClickCount() % 2 == 0 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown()) {
			e.consume();

			open();
		}
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			showContextMenu(e);
		}
	}

	private static void showContextMenu(MouseEvent e) {
		ExplorerItemPopup menu = new ExplorerItemPopup();
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
}

class ExplorerItemPopup extends JPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;

	public ExplorerItemPopup() {
		add(MenuItems.newMenu("New                    "));
		addSeparator();

		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8437185508877673148L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(int i = 0; i < Explorer.selectedLabels.size(); i++) {
					Explorer.selectedLabels.get(i).open();
				}
			}
		});

		add(openItem);

		JMenuItem openInSystemItem = new JMenuItem("Show in System Explorer");
		openInSystemItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6207282331220892917L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(Explorer.selectedLabels.size() != 1) return;
					Runtime.getRuntime().exec("explorer.exe /select," + Explorer.selectedLabels.get(0).parent.path);
				} catch (IOException e) {
					e.printStackTrace(new PrintWriter(Window.consoleOut));
				}
			}
		});

		add(openInSystemItem);
		addSeparator();
		add(MenuItems.fileItem(FileMenuItem.COPY));
		add(MenuItems.fileItem(FileMenuItem.PASTE));
		add(MenuItems.fileItem(FileMenuItem.DELETE));
		addSeparator();
		JMenu refactorMenu = new JMenu("Refactor");

		JMenuItem renameItem = MenuItems.fileItem(FileMenuItem.RENAME);
		renameItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 382967017949373966L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(Explorer.selectedLabels.size() != 1) return;

				String path = Explorer.selectedLabels.get(0).parent.path;
				String name = new File(path).getName();
				String rawName = StringUtil.stripExtension(name);
				final String extension = name.replaceAll(rawName, "");
				final String pathToParent = path.substring(0, path.lastIndexOf(name));

				String newName = StringPrompt.prompt("Rename", "Enter a new name for the file:", rawName,
						new StringValidator() {
							@Override
							public boolean validate(String str) {
								return str.trim().length() > 0 && FileUtil.validateFilename(str)
										&& !new File(pathToParent + str + extension).exists();
							}
						});

				if (newName != null) {
					if (ProjectManager.renameFile(new File(path), newName)) {
						ExplorerItem parentItem = Explorer.selectedLabels.get(0).parent;
						parentItem.path = pathToParent + newName + extension;
						if (parentItem.parent != null) {
							parentItem.parent.collapse();
							parentItem.parent.expand();
						} else {
							Window.explorer.generateProjectList();
						}

						TabManager.renameTab(path, pathToParent + newName + extension);

					} else {
						JOptionPane.showMessageDialog(null,
								"<html>The action can't be completed because the folder or file is open in another program.<br>Close the folder and try again.</html>",
								"An error occurred.", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		refactorMenu.add(renameItem);
		refactorMenu.add(MenuItems.fileItem(FileMenuItem.MOVE));

		add(refactorMenu);
	}
}