package com.energyxxer.cbe;

import com.energyxxer.util.FileUtil;

public class Preferences {
	
	private static java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
	public static final String DEFAULT_WORKSPACE_PATH = FileUtil.normalize("C:/Program Files/Command Block Engine/workspace");
	static{
		if(prefs.get("workspace_dir", null) == null) {
			prefs.put("workspace_dir", WorkspaceSelector.prompt());
		}
	}
	
	public static String get(String key, String def) {
		return prefs.get(key, def);
	}
	
	public static String get(String key) {
		return prefs.get(key, null);
	}
	
	public static void put(String key, String value) {
		prefs.put(key, value);
	}
	
	public static void remove(String key) {
		prefs.remove(key);
	}
}
