package com.energyxxer.craftr.ui.common;

import com.energyxxer.craftr.files.FileType;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenu;
import com.energyxxer.craftr.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.util.ImageManager;

import javax.swing.ImageIcon;

/**
 * Provides managers that create menu components for file and project management.
 */
public class MenuItems {
	public static StyledMenu newMenu(String title) {
		StyledMenu newMenu = new StyledMenu(title);
		newMenu.setIcon(new ImageIcon(
				ImageManager.load("/assets/icons/light_theme/Craftr.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));

		// --------------------------------------------------

		StyledMenuItem projectItem = new StyledMenuItem("Project        ", "project");
		projectItem.addActionListener(e -> FileType.PROJECT.create(null));

		newMenu.add(projectItem);

		// --------------------------------------------------

		newMenu.addSeparator();

		// --------------------------------------------------
		
		StyledMenuItem packageItem = new StyledMenuItem("Package", "package");
		packageItem.addActionListener(e -> FileType.PACKAGE.create(Preferences.get("workspace_dir")));

		newMenu.add(packageItem);

		// --------------------------------------------------

		newMenu.addSeparator();

		// --------------------------------------------------

		StyledMenuItem worldItem = new StyledMenuItem("World", "world");
		//worldItem.addActionListener(e -> FileFactory.newUnit(Preferences.get("workspace_dir"), FileType.WORLD));

		newMenu.add(worldItem);

		// --------------------------------------------------

		newMenu.addSeparator();

		// --------------------------------------------------

		StyledMenuItem entityItem = new StyledMenuItem("Entity", "entity");
		//entityItem.addActionListener(e -> FileFactory.newUnit(Preferences.get("workspace_dir"), FileType.ENTITY));
		
		newMenu.add(entityItem);

		// --------------------------------------------------

		StyledMenuItem itemItem = new StyledMenuItem("Item", "item");
		//itemItem.addActionListener(e -> FileFactory.newUnit(Preferences.get("workspace_dir"), FileType.ITEM));

		newMenu.add(itemItem);

		// --------------------------------------------------

		StyledMenuItem featureItem = new StyledMenuItem("Feature", "feature");
		//featureItem.addActionListener(e -> FileFactory.newUnit(Preferences.get("workspace_dir"), FileType.FEATURE));

		newMenu.add(featureItem);

		// --------------------------------------------------

		StyledMenuItem classItem = new StyledMenuItem("Class", "class");
		//classItem.addActionListener(e -> FileFactory.newUnit(Preferences.get("workspace_dir"), FileType.CLASS));

		newMenu.add(classItem);
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
			/*item.setEnabled(false);
			item.setEnabled(ExplorerMaster.selectedLabels.size() > 0);
			item.addActionListener(e -> {
				ArrayList<File> files = new ArrayList<>();
				String fileType = null;
				for(int i = 0; i < ExplorerMaster.selectedLabels.size(); i++) {
					File file = new File(ExplorerMaster.selectedLabels.get(i).parent.path);
					if(file.isFile() && fileType == null) {
						fileType = "file";
					} else if(file.isDirectory() && fileType == null) {
						fileType = "package";
					} else if(file.isDirectory() && "file".equals(fileType)) {
						fileType = "item";
					} else if(file.isFile() && "package".equals(fileType)) {
						fileType = "item";
					}
					files.add(file);
				}

				String subject = ((ExplorerMaster.selectedLabels.size() == 1) ? "this" : "these") + " " + ((ExplorerMaster.selectedLabels.size() == 1) ? "" : "" + ExplorerMaster.selectedLabels.size() + " ") + fileType + ((ExplorerMaster.selectedLabels.size() == 1) ? "" : "s");

				int confirmation = JOptionPane.showConfirmDialog(null,
						"        Are you sure you want to delete " + subject + "?        ",
						"Delete " + fileType, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirmation == JOptionPane.YES_OPTION) {
					for(File file : files) FileUtil.deleteFolder(file);
					CraftrWindow.projectExplorer.refresh();
				}
			});*/
			break;
		case MOVE:
			item = new StyledMenuItem("Move");
			item.setEnabled(CraftrWindow.projectExplorer.getSelectedFiles().size() > 0);
			break;
		case PASTE:
			item = new StyledMenuItem("Paste");
			break;
		case RENAME:
			item = new StyledMenuItem("Rename", "rename");
			item.setEnabled(CraftrWindow.projectExplorer.getSelectedFiles().size() == 1);
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
