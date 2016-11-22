package com.energyxxer.cbe.ui.theme.change;

import java.util.ArrayList;

import com.energyxxer.cbe.ui.theme.Theme;

public interface ThemeChangeListener {
	
	static ArrayList<ThemeChangeListener> listeners = new ArrayList<ThemeChangeListener>();
	
	public static void addThemeChangeListener(ThemeChangeListener l) {
		listeners.add(l);
	}
	
	public static void dispatchThemeChange(Theme t) {
		for(ThemeChangeListener i : listeners) {
			i.themeChanged(t);
		}
	}
	
	public void themeChanged(Theme t);
}
