package com.energyxxer.cbe.ui.common;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.energyxxer.cbe.files.FileFactory;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.util.FileUtil;
import com.energyxxer.cbe.util.ImageManager;

/**
 * Provides methods that create menu components for file and project management.
 */
public class MenuItems {
	public static JMenu newMenu(String title) {
		JMenu newMenu = new JMenu(title);
		newMenu.setIcon(new ImageIcon(
				ImageManager.load("/assets/icons/light_theme/cbe.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

		// --------------------------------------------------

		JMenuItem projectItem = new JMenuItem("Project        ", new ImageIcon(ImageManager
				.load("/assets/icons/light_theme/project_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		projectItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1211067464491607872L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFactory.newProject();
			}
		});

		newMenu.add(projectItem);

		// --------------------------------------------------

		newMenu.addSeparator();

		// --------------------------------------------------
		
		JMenuItem folderItem = new JMenuItem("Folder", new ImageIcon(ImageManager
				.load("/assets/icons/light_theme/folder_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		folderItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1211067464491607872L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFactory.newFolder();
			}
		});

		newMenu.add(folderItem);

		// --------------------------------------------------

		newMenu.addSeparator();

		// --------------------------------------------------

		JMenuItem entityItem = new JMenuItem("Entity", new ImageIcon(ImageManager.load("/assets/icons/light_theme/entity_new.png")
				.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		entityItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1211067464491607872L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFactory.newEntity();
			}
		});
		
		newMenu.add(entityItem);

		// --------------------------------------------------

		JMenuItem itemItem = new JMenuItem("Item", new ImageIcon(ImageManager.load("/assets/icons/light_theme/item_new.png")
				.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
		itemItem.addActionListener(new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1211067464491607872L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFactory.newItem();
			}
		});
		
		newMenu.add(itemItem);
		return newMenu;
	};

	public enum FileMenuItem {
		COPY, PASTE, DELETE, RENAME, MOVE
	}

	public static JMenuItem fileItem(FileMenuItem type) {
		JMenuItem item = null;
		switch (type) {
		case COPY:
			item = new JMenuItem("Copy");
			break;
		case DELETE:
			item = new JMenuItem("Delete");
			item.setEnabled(Explorer.selectedLabels.size() > 0);
			item.addActionListener(new AbstractAction() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 219892145361786637L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					ArrayList<File> files = new ArrayList<File>();
					String fileType = null;
					for(int i = 0; i < Explorer.selectedLabels.size(); i++) {
						File file = new File(Explorer.selectedLabels.get(i).parent.path);
						if(file.isFile() && fileType == null) {
							fileType = "file";
						} else if(file.isDirectory() && fileType == null) {
							fileType = "folder";
						} else if(file.isDirectory() && fileType == "file") {
							fileType = "item";
						} else if(file.isFile() && fileType == "folder") {
							fileType = "item";
						}
						files.add(file);
					}
					
					String subject = ((Explorer.selectedLabels.size() == 1) ? "this" : "these") + " " + ((Explorer.selectedLabels.size() == 1) ? "" : "" + Explorer.selectedLabels.size() + " ") + fileType + ((Explorer.selectedLabels.size() == 1) ? "" : "s");
					
					int confirmation = JOptionPane.showConfirmDialog(null,
							"        Are you sure you want to delete " + subject + "?        ",
							"Delete " + fileType, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirmation == JOptionPane.YES_OPTION) {
						for(int i = 0; i < files.size(); i++) {
							File file = files.get(i);
							FileUtil.deleteFolder(file);
						}
						Window.explorer.generateProjectList();
					}
				}
			});
			break;
		case MOVE:
			item = new JMenuItem("Move");
			item.setEnabled(Explorer.selectedLabels.size() > 0);
			break;
		case PASTE:
			item = new JMenuItem("Paste");
			break;
		case RENAME:
			item = new JMenuItem("Rename", new ImageIcon(ImageManager.load("/assets/icons/light_theme/rename.png")
					.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			item.setEnabled(Explorer.selectedLabels.size() == 1);
			break;
		default:
			break;
		}
		return item;
	}

	public static JMenu refactorMenu(String title) {
		JMenu newMenu = new JMenu(title);

		newMenu.add(fileItem(FileMenuItem.RENAME));
		newMenu.add(fileItem(FileMenuItem.MOVE));

		return newMenu;

	}
}
