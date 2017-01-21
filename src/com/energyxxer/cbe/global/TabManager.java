package com.energyxxer.cbe.global;

import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.Tab;
import com.energyxxer.cbe.ui.TabComponent;
import com.energyxxer.cbe.ui.editor.behavior.caret.CaretProfile;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

	private static TabComponent selectedTab = null;
	
	private static JPopupMenu menu = new JPopupMenu();

	public static void openTab(String path, int index) {
		openTab(path);
		selectLocation(selectedTab.getLinkedTab(), index, 0);
	}

	public static void openTab(String path, int index, int length) {
		openTab(path);
		selectLocation(selectedTab.getLinkedTab(), index, length);
	}

	public static void openTab(String path) {
		for (int i = 0; i < openTabs.size(); i++) {
			if (openTabs.get(i).path.equals(path)) {
				setSelectedTab(openTabs.get(i));
				return;
			}
		}
		openTabs.add(new Tab(path));
		setSelectedTab(openTabs.get(openTabs.size() - 1));
		
	}

	public static void selectLocation(Tab tab, int index, int length) {
		tab.editor.editorComponent.requestFocus();
		tab.editor.editorComponent.getCaret().setProfile(new CaretProfile(index + length, index));
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
			if(!tab.getLinkedTabComponent().isSaved()) {
				int confirmation = JOptionPane.showOptionDialog(
						null,
						"'" + tab.getLinkedTabComponent().getName() + "' has changes; do you want to save them?",
						"Unsaved changes",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						new String[] {"Save", "Don't Save", "Cancel"},
						"Cancel"
				);
				if (confirmation == 0) {
					tab.save();
				}
				if(confirmation == 2 || confirmation == -1) return;
			}
		}
		for (int i = 0; i < openTabs.size(); i++) {
			if (openTabs.get(i) == tab) {
				boolean closedActive = false;
				if (selectedTab == openTabs.get(i).getLinkedTabComponent())
					closedActive = true;
				openTabs.get(i).getLinkedTabComponent().getParent().remove(openTabs.get(i).getLinkedTabComponent());
				Window.editArea.tabList.revalidate();
				Window.editArea.tabList.repaint();
				openTabs.remove(i);
				if (closedActive) {
					if (openTabs.size() == 0) {
						setSelectedTab(null);
					} else if (openTabs.size() == 1) {
						setSelectedTab(openTabs.get(0));
					} else if (openTabs.size() > 1) {
						if (i >= openTabs.size()) {
							setSelectedTab(openTabs.get(i - 1));
						} else if (i <= 0) {
							setSelectedTab(openTabs.get(i));
						} else {
							if (openTabs.get(i - 1).openedTimeStamp >= openTabs.get(i).openedTimeStamp) {
								setSelectedTab(openTabs.get(i - 1));
							} else {
								setSelectedTab(openTabs.get(i));
							}
						}
					}
				}
				return;
			}
		}
	}
	
	public static void updateMenu() {
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
			JMenuItem item = new JMenuItem(((!tab.getLinkedTabComponent().isSaved()) ? "*" : "") + tab.getLinkedTabComponent().getName());
			item.setIcon(tab.getLinkedTabComponent().getIcon());
			if(!tab.visible) {
				item.setFont(item.getFont().deriveFont(Font.BOLD));
			}
			item.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					tab.getLinkedTabComponent().mousePressed(e);
				}
				public void mouseReleased(MouseEvent e) {
					tab.getLinkedTabComponent().mouseReleased(e);
				}
			});
			menu.add(item);
		}
	}
	
	public static JPopupMenu getMenu() {
		updateTabVisibility();
		updateMenu();
		return menu;
	}

	public static void setSelectedTab(Tab tab) {
		if (selectedTab != null) {
			selectedTab.selected = false;
			Window.editArea.remove(selectedTab.getLinkedTab().editor);
		}
		selectedTab = null;
		if (tab != null) {
			selectedTab = tab.getLinkedTabComponent();
			
			if(!tab.visible) {
				openTabs.indexOf(tab);
				if(openTabs.indexOf(tab) >= 0) {
					openTabs.remove(openTabs.indexOf(tab));
					openTabs.add(0,tab);
					Window.editArea.tabList.add(tab.getLinkedTabComponent(), 0);
				}
			}
			tab.getLinkedTabComponent().selected = true;
			
			tab.onSelect();
			Window.editArea.add(tab.editor, BorderLayout.CENTER);
		} else {
            Window.statusBar.setCaretInfo(Commons.DEFAULT_CARET_DISPLAY_TEXT);
		}

		Window.editArea.revalidate();
		Window.editArea.repaint();
	}

	public static void addTabComponent(TabComponent tab) {
		Window.editArea.tabList.add(tab);
		Window.editArea.tabList.revalidate();
		Window.editArea.tabList.repaint();
	}
	
	private static void updateTabVisibility() {
		for(int i = 0; i < openTabs.size(); i++) {
			Tab tab = openTabs.get(i);
			TabComponent tabComponent = tab.getLinkedTabComponent();
			tab.visible = tabComponent.getY() <= 0;
		}
	}

	public static Tab getSelectedTab() {
		if (selectedTab == null)
			return null;
		return selectedTab.getLinkedTab();
	}

	public static void renameTab(String oldPath, String newPath) {
		File newFile = new File(newPath);
		if (newFile.isFile()) {
			for (int i = 0; i < openTabs.size(); i++) {
				if (openTabs.get(i).path.equals(oldPath)) {
					openTabs.get(i).path = newPath;
					openTabs.get(i).updateName();
				}
			}
		} else if (newFile.isDirectory()) {
			for (int i = 0; i < openTabs.size(); i++) {
				if (openTabs.get(i).path.startsWith(oldPath)) {
					openTabs.get(i).path = newPath + openTabs.get(i).path.substring(oldPath.length());
					openTabs.get(i).updateName();
				}
			}
		}
	}
}
