package com.energyxxer.craftr.ui.theme;

import java.awt.Color;
import java.util.HashMap;

import static com.energyxxer.craftr.ui.theme.Theme.ThemeType.GUI_THEME;

public class Theme {
	private final String name;
	private final ThemeType themeType;
	private final HashMap<String, Object> values;

	public enum ThemeType {
		GUI_THEME("gui"), SYNTAX_THEME("syntax");

		public String subdirectory;

		ThemeType(String subdirectory) {
			this.subdirectory = subdirectory;
		}
	}

	public Theme(String name) {
		this(name, new HashMap<>());
	}
	public Theme(String name, HashMap<String, Object> values) {
		this(GUI_THEME, name, values);
	}
	public Theme(ThemeType type, String name, HashMap<String, Object> values) {
		this.name = name;
		this.values = values;
		this.themeType = type;
	}

	protected void put(String key, Object value) {
		values.put(key,value);
	}

	public Object get(String key, Object defaultValue) {
		Object value = values.get(key);
		return (value != null) ? value : defaultValue;
	}

	public Color getColor(String key, Color defaultValue) {
		Object value = values.get(key);
		if(value == null) return defaultValue;
		if(value instanceof Color) return (Color) value;
		else return defaultValue;
	}

	public Color getColor(String key) { return getColor(key, null); }

	public boolean getBoolean(String key, boolean defaultValue) {
		Object value = values.get(key);
		if(value == null) return defaultValue;
		if(value instanceof Boolean) return (Boolean) value;
		else return defaultValue;
	}

	public boolean getBoolean(String key) { return getBoolean(key, false); }

	public String getString(String key, String defaultValue) {
		Object value = values.get(key);
		if(value == null) return defaultValue;
		if(value instanceof String) return (String) value;
		else return defaultValue;
	}

	public String getString(String key) { return getString(key, null); }

	public int getInteger(String key, int defaultValue) {
		Object value = values.get(key);
		if(value == null) return defaultValue;
		if(value instanceof Integer) return (Integer) value;
		else return defaultValue;
	}

	public int getInteger(String key) { return getInteger(key, 0); }

	public String getName() {return name;}

	public ThemeType getThemeType() {
		return themeType;
	}

	public HashMap<String, Object> getValues() {
		return values;
	}

	@Override
	public String toString() {return name;}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Theme theme = (Theme) o;

		return name != null ? name.equals(theme.name) : theme.name == null;
	}
}