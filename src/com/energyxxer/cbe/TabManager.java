package com.energyxxer.cbe;

import java.awt.BorderLayout;
import java.util.ArrayList;

import com.energyxxer.ui.TabComponent;

public class TabManager {
	
	public static ArrayList<Tab> openTabs = new ArrayList<Tab>();
	
	private static TabComponent selectedTab = null;
	
	public static void openTab(String path) {
		for(int i = 0; i < openTabs.size(); i++) {
			if(openTabs.get(i).path == path) {
				setSelectedTab(openTabs.get(i));
				return;
			}
		}
		openTabs.add(new Tab(path));
		setSelectedTab(openTabs.get(openTabs.size()-1));
	}
	
	public static void closeTab(Tab tab) {
		for(int i = 0; i < openTabs.size(); i++) {
			if(openTabs.get(i) == tab) {
				boolean closedActive = false;
				if(selectedTab == openTabs.get(i).getLinkedTabComponent()) closedActive = true;
				openTabs.get(i).getLinkedTabComponent().getParent().remove(openTabs.get(i).getLinkedTabComponent());
				Window.tabList.revalidate();
				Window.tabList.repaint();
				openTabs.remove(i);
				if(closedActive) {
					if(openTabs.size() == 0) {
						setSelectedTab(null);
					} else if(openTabs.size() == 1) {
						setSelectedTab(openTabs.get(0));
					} else if(openTabs.size() > 1) {
						if(i >= openTabs.size()) {
							setSelectedTab(openTabs.get(i-1));
						} else if(i <= 0) {
							setSelectedTab(openTabs.get(i));
						} else {
							if(openTabs.get(i-1).openedTimeStamp >= openTabs.get(i).openedTimeStamp) {
								setSelectedTab(openTabs.get(i-1));
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

	public static void setSelectedTab(Tab tab) {
		if(selectedTab != null) {
			selectedTab.selected = false;
			selectedTab.repaint();
			
			Window.edit_area.remove(selectedTab.getLinkedTab().editor);
		}
		selectedTab = null;
		if(tab != null) {
			selectedTab = tab.getLinkedTabComponent();
			tab.getLinkedTabComponent().selected = true;
			tab.getLinkedTabComponent().repaint();
			tab.onSelect();
			Window.edit_area.add(tab.editor,BorderLayout.CENTER);
		}
		
		Window.edit_area.revalidate();
		Window.edit_area.repaint();
	}
	
	public static void addTabComponent(TabComponent tab) {
		Window.tabList.add(tab);
		Window.tabList.revalidate();
		Window.tabList.repaint();
	}
	
	public static Tab getSelectedTab() {
		if(selectedTab == null) return null;
		return selectedTab.getLinkedTab();
	}
}
