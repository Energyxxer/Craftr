package com.energyxxer.ui.common;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.energyxxer.ui.explorer.Explorer;
import com.energyxxer.util.ImageManager;

public class MenuItems {
	public static JMenu newMenu(String title) {
		JMenu newMenu = new JMenu(title);
		newMenu.setIcon(new ImageIcon(ImageManager.load("/assets/icons/cbe.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

			//--------------------------------------------------
		
			JMenuItem projectItem = new JMenuItem("Project        ",new ImageIcon(ImageManager.load("/assets/icons/project_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

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
