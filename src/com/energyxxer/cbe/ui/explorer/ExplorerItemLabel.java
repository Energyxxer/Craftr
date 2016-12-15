package com.energyxxer.cbe.ui.explorer;

import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.common.MenuItems;
import com.energyxxer.cbe.ui.common.MenuItems.FileMenuItem;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenu;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.styledcomponents.StyledPopupMenu;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Non-recursive label for an explorer item.
 */
public class ExplorerItemLabel extends JLabel implements MouseListener, ThemeChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3655326885011257489L;

	private boolean rollover = false;
	private boolean selected = false;

	private Color rollover_bg;
	private Color rollover_border;
	private Color selected_bg;
	private Color selected_border;

	public ExplorerItem parent;

	public ExplorerItemLabel(File file, ExplorerItem parent) {
		super(file.getName());
		this.parent = parent;
		updateLabel();
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		this.addThemeChangeListener();
	}

	public void updateLabel() {
		File file = new File(parent.path);
		setText(file.getName());
		String icon = ProjectManager.getIconFor(file);
		if (file.isDirectory()) {
			if(icon != null)
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + icon + ".png").getScaledInstance(16,16, Image.SCALE_SMOOTH)));
			else this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + "folder.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			try {
				File workspaceFile = new File(Preferences.get("workspace_dir"));
				if (file.getParentFile().getCanonicalPath() == workspaceFile.getCanonicalPath()) {
					this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + "project.png").getScaledInstance(16, 16,
							java.awt.Image.SCALE_SMOOTH)));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (file.getName().endsWith(".mcbe")) {
				if(icon == null) icon = Commons.themeAssetsPath + "warn";
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + icon + ".png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
			} else {
				this.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Commons.themeAssetsPath + "file.png").getScaledInstance(16, 16,
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
			g2.setColor(selected_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2.setColor(selected_bg);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
/*
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
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 1, 1);*/
		} else if (rollover) {
			g2.setColor(rollover_border);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2.setColor(rollover_bg);
			g2.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
			/*
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
			g2.fillRect(this.getWidth() - 2, this.getHeight() - 2, 1, 1);*/
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

	@Override
	public void themeChanged(Theme t) {
		this.setFont(new Font(t.getString("Explorer.label.font",t.getString("General.font","Tahoma")), 0, 12));
		this.setForeground(t.getColor("Explorer.label.foreground",t.getColor("General.foreground", Color.BLACK)));

		this.rollover_bg = t.getColor("Explorer.label.hover.background",new Color(235, 235, 235));
		this.rollover_border = t.getColor("Explorer.label.hover.border",new Color(200, 200, 200));
		this.selected_bg = t.getColor("Explorer.label.selected.background",new Color(235, 235, 235));
		this.selected_border = t.getColor("Explorer.label.selected.border",new Color(200, 200, 200));

		updateLabel();
	}
}

class ExplorerItemPopup extends StyledPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968631495164738852L;

	public ExplorerItemPopup() {
		add(MenuItems.newMenu("New                    "));
		addSeparator();

		StyledMenuItem openItem = new StyledMenuItem("Open");
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

		StyledMenuItem openInSystemItem = new StyledMenuItem("Show in System Explorer");
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
		StyledMenu refactorMenu = new StyledMenu("Refactor");

		StyledMenuItem renameItem = MenuItems.fileItem(FileMenuItem.RENAME);
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