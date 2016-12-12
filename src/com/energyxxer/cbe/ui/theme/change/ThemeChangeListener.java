package com.energyxxer.cbe.ui.theme.change;

import java.util.ArrayList;

import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.theme.Theme;

public interface ThemeChangeListener {
	
	ArrayList<ThemeChangeListener> listeners = new ArrayList<ThemeChangeListener>();

	static void addThemeChangeListener(ThemeChangeListener l) {
		listeners.add(l); l.themeChanged(Window.theme);
	}

	default void addThemeChangeListener() {
		addThemeChangeListener(this);
	}
	
	static void dispatchThemeChange(Theme t) {
		for(ThemeChangeListener i : listeners) {
			i.themeChanged(t);
		}
	}
	
	void themeChanged(Theme t);
}
