package com.energyxxer.craftr.global;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.dialogs.OptionDialog;
import com.energyxxer.craftr.ui.editor.CraftrEditor;
import com.energyxxer.craftr.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.util.ImageManager;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Interface that allows communication between parts of the program and the tab
 * list.
 */
public class TabManager {

	public static ArrayList<Tab> openTabs = new ArrayList<>();

	private static Tab selectedTab = null;
	
	private static JPopupMenu menu = new JPopupMenu();

	public static void openTab(String path, int index) {
		openTab(path);
		selectLocation(selectedTab, index, 0);
	}

	public static void openTab(String path, int index, int length) {
		openTab(path);
		selectLocation(selectedTab, index, length);
	}

	public static void openTab(String path) {
		for (int i = 0; i < openTabs.size(); i++) {
			if (openTabs.get(i).path.equals(path)) {
				setSelectedTab(openTabs.get(i));
				return;
			}
		}
		Tab nt = new Tab(path);
		openTabs.add(nt);
		CraftrWindow.tabList.addTab(nt);
		setSelectedTab(nt);
	}

	private static void selectLocation(Tab tab, int index, int length) {
		if(tab.module instanceof CraftrEditor) {
			((CraftrEditor) tab.module).editorComponent.getCaret().setProfile(new CaretProfile(index + length, index));
		}
	}

	public static void closeSelectedTab() {
		closeSelectedTab(false);
	}

	public static void closeSelectedTab(boolean force) {
		closeTab(getSelectedTab(), force);
	}

	public static void closeTab(Tab tab) {
		closeTab(tab, false);
	}

	public static void closeTab(Tab tab, boolean force) {
		if(tab == null) return;
		if(!force) {
			if(!tab.isSaved()) {
				String confirmation = new OptionDialog("Unsaved changes", "'" + tab.getName() + "' has changes; do you want to save them?", new String[] {"Save", "Don't Save", "Cancel"}).result;
				if("Save".equals(confirmation)) {
					tab.save();
				}
				if(confirmation == null || "Cancel".equals(confirmation)) return;
			}
		}
		for (int i = 0; i < openTabs.size(); i++) {
			if (openTabs.get(i) == tab) {
				if (selectedTab == openTabs.get(i)) setSelectedTab(CraftrWindow.tabList.getFallbackTab(tab));

				CraftrWindow.tabList.removeTab(tab);
				openTabs.remove(i);

				return;
			}
		}
	}
	
	private static void updateMenu() {
		menu.removeAll();
		if(TabManager.openTabs.size() <= 0) {
			JMenuItem item = new JMenuItem("No tabs open!");
			item.setFont(item.getFont().deriveFont(Font.ITALIC));
			item.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/close.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			menu.add(item);
			return;
		}
		for(int i = 0; i < TabManager.openTabs.size(); i++) {
			Tab tab = TabManager.openTabs.get(i);
			JMenuItem item = new JMenuItem(((!tab.isSaved()) ? "*" : "") + tab.getName());
			item.setIcon(new ImageIcon(tab.getLinkedTabItem().getIcon()));
			if(!tab.visible) {
				item.setFont(item.getFont().deriveFont(Font.BOLD));
			}
			item.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					setSelectedTab(tab);
				}
			});
			menu.add(item);
		}
	}
	
	public static JPopupMenu getMenu() {
		updateMenu();
		return menu;
	}

	public static void setSelectedTab(Tab tab) {
		CraftrWindow.tabList.selectTab(tab);
		if (selectedTab != null) {
			CraftrWindow.editArea.remove(selectedTab.getModuleComponent());
			selectedTab = null;
		}
		if (tab != null) {
			selectedTab = tab;
			
			Project linkedProject = tab.getLinkedProject();
			CraftrWindow.setTitle(((linkedProject != null) ? linkedProject.getName() + " - " : "") + tab.getName());
			CraftrWindow.editArea.add(tab.getModuleComponent(), BorderLayout.CENTER);
			tab.onSelect();
		} else {
			CraftrWindow.statusBar.setCaretInfo(Commons.DEFAULT_CARET_DISPLAY_TEXT);
			CraftrWindow.statusBar.setSelectionInfo(" ");
            CraftrWindow.clearTitle();
		}

		CraftrWindow.editArea.revalidate();
		CraftrWindow.editArea.repaint();
	}

	public static Tab getSelectedTab() {
		return selectedTab;
	}

	public static void renameTab(String oldPath, String newPath) {
		File newFile = new File(newPath);
		if (newFile.isFile()) {
			for(Tab tab : openTabs) {
				if (tab.path.equals(oldPath)) {
					tab.path = newPath;
					tab.updateName();
				}
			}
		} else if (newFile.isDirectory()) {
			for(Tab tab : openTabs) {
				if (tab.path.startsWith(oldPath)) {
					tab.path = newPath + tab.path.substring(oldPath.length());
					tab.updateName();
				}
			}
		}
	}
}
