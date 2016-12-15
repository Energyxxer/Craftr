package com.energyxxer.cbe.ui.common;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.energyxxer.cbe.files.FileFactory;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenu;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.util.FileUtil;
import com.energyxxer.cbe.util.ImageManager;

/**
 * Provides methods that create menu components for file and project management.
 */
public class MenuItems {
	public static StyledMenu newMenu(String title) {
		StyledMenu newMenu = new StyledMenu(title);
		newMenu.setIcon(new ImageIcon(
				ImageManager.load("/assets/icons/light_theme/cbe.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

		// --------------------------------------------------

		StyledMenuItem projectItem = new StyledMenuItem("Project        ", "project_new");
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
		
		StyledMenuItem folderItem = new StyledMenuItem("Folder", "folder_new");
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

		StyledMenuItem entityItem = new StyledMenuItem("Entity", "entity_new");
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

		StyledMenuItem itemItem = new StyledMenuItem("Item", "item_new");
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
	}

	public enum FileMenuItem {
		COPY, PASTE, DELETE, RENAME, MOVE
	}

	public static StyledMenuItem fileItem(FileMenuItem type) {
		StyledMenuItem item = null;
		switch (type) {
		case COPY:
			item = new StyledMenuItem("Copy");
			break;
		case DELETE:
			item = new StyledMenuItem("Delete");
			item.setEnabled(Explorer.selectedLabels.size() > 0);
			item.addActionListener(e -> {
				ArrayList<File> files = new ArrayList<>();
				String fileType = null;
				for(int i = 0; i < Explorer.selectedLabels.size(); i++) {
					File file = new File(Explorer.selectedLabels.get(i).parent.path);
					if(file.isFile() && fileType == null) {
						fileType = "file";
					} else if(file.isDirectory() && fileType == null) {
						fileType = "folder";
					} else if(file.isDirectory() && "file".equals(fileType)) {
						fileType = "item";
					} else if(file.isFile() && "folder".equals(fileType)) {
						fileType = "item";
					}
					files.add(file);
				}

				String subject = ((Explorer.selectedLabels.size() == 1) ? "this" : "these") + " " + ((Explorer.selectedLabels.size() == 1) ? "" : "" + Explorer.selectedLabels.size() + " ") + fileType + ((Explorer.selectedLabels.size() == 1) ? "" : "s");

				int confirmation = JOptionPane.showConfirmDialog(null,
						"        Are you sure you want to delete " + subject + "?        ",
						"Delete " + fileType, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirmation == JOptionPane.YES_OPTION) {
					for(File file : files) FileUtil.deleteFolder(file);
					Window.explorer.generateProjectList();
				}
			});
			break;
		case MOVE:
			item = new StyledMenuItem("Move");
			item.setEnabled(Explorer.selectedLabels.size() > 0);
			break;
		case PASTE:
			item = new StyledMenuItem("Paste");
			break;
		case RENAME:
			item = new StyledMenuItem("Rename", "rename");
			item.setEnabled(Explorer.selectedLabels.size() == 1);
			break;
		default:
			break;
		}
		return item;
	}

	public static StyledMenu refactorMenu(String title) {
		StyledMenu newMenu = new StyledMenu(title);

		newMenu.add(fileItem(FileMenuItem.RENAME));
		newMenu.add(fileItem(FileMenuItem.MOVE));

		return newMenu;

	}
}
