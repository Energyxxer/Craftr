package com.energyxxer.craftr.ui.theme;

import java.awt.*;
import java.util.HashMap;

public class Theme {
	private String name;
	private HashMap<String, Object> values = new HashMap<>();

	public Theme(String name) {
		this.name = name;
	}
	public Theme(String name, HashMap<String, Object> values) {
		this.name = name;
		this.values = values;
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