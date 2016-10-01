package com.energyxxer.ui.common;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.energyxxer.cbe.Window;
import com.energyxxer.files.FileFactory;
import com.energyxxer.ui.explorer.Explorer;
import com.energyxxer.util.FileUtil;
import com.energyxxer.util.ImageManager;

/**
 * Provides methods that create menu components for file and project management.
 * */
public class MenuItems {
	public static JMenu newMenu(String title) {
		JMenu newMenu = new JMenu(title);
		newMenu.setIcon(new ImageIcon(ImageManager.load("/assets/icons/cbe.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

			//--------------------------------------------------
		
			JMenuItem projectItem = new JMenuItem("Project        ",new ImageIcon(ImageManager.load("/assets/icons/project_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			projectItem.addActionListener(new AbstractAction() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1211067464491607872L;

				public void actionPerformed(ActionEvent arg0) {
					FileFactory.newProject();
				}
			});

			newMenu.add(projectItem);
			

			//--------------------------------------------------
			
			newMenu.addSeparator();
			
			//--------------------------------------------------

			JMenuItem entityItem = new JMenuItem("Entity",new ImageIcon(ImageManager.load("/assets/icons/entity_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			
			newMenu.add(entityItem);
			
			//--------------------------------------------------
			
			JMenuItem itemItem = new JMenuItem("Item", new ImageIcon(ImageManager.load("/assets/icons/item_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			
			newMenu.add(itemItem);
			return newMenu;
	};
	
	public enum FileMenuItem {
		COPY,PASTE,DELETE,RENAME,MOVE
	}
	
	public static JMenuItem fileItem(FileMenuItem type) {
		JMenuItem item = null;
		switch(type) {
			case COPY:
				item = new JMenuItem("Copy");
				break;
			case DELETE:
				item = new JMenuItem("Delete");
				item.setEnabled(Explorer.selectedLabel != null);
				item.addActionListener(new AbstractAction() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 219892145361786637L;

					public void actionPerformed(ActionEvent arg0) {
						File file = new File(Explorer.selectedLabel.parent.path);			
						String fileType = (file.isDirectory()) ? "folder" : "file";
						int confirmation = JOptionPane.showConfirmDialog(null, "        Are you sure you want to delete this " + fileType + "?        ", "Delete " + fileType, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(confirmation == JOptionPane.YES_OPTION) {
							FileUtil.deleteFolder(file);
							Window.explorer.generateProjectList();
						}
					}
				});
				break;
			case MOVE:
				item = new JMenuItem("Move");
				item.setEnabled(Explorer.selectedLabel != null);
				break;
			case PASTE:
				item = new JMenuItem("Paste");
				break;
			case RENAME:
				item = new JMenuItem("Rename", new ImageIcon(ImageManager.load("/assets/icons/rename.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				item.setEnabled(Explorer.selectedLabel != null);
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
