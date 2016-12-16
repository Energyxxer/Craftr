package com.energyxxer.cbe.ui.theme.change;

import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.ui.theme.Theme;

import java.util.ArrayList;

public interface ThemeChangeListener {
	
	ArrayList<ThemeChangeListener> listeners = new ArrayList<ThemeChangeListener>();

	static void addThemeChangeListener(ThemeChangeListener l) {
		addThemeChangeListener(l, false);
	}

	static void addThemeChangeListener(ThemeChangeListener l, boolean priority) {
		if(priority) listeners.add(0, l);
		else listeners.add(l);

		l.themeChanged(Window.getTheme());
	}

	default void addThemeChangeListener() {
		addThemeChangeListener(this);
	}
	
	static void dispatchThemeChange(Theme t) {
		for(int i = 0; i < listeners.size(); i++) {
			listeners.get(i).themeChanged(t);
		}
	}
	
	void themeChanged(Theme t);
}
