package com.energyxxer.cbe.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Things for strings.
 */
public class StringUtil {

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static String ellipsis(String str, int max) {
		if (str.length() > max) {
			return (str.substring(0, max - 3) + "...").intern();
		} else {
			return str;
		}
	}

	public static String substring(String str, int i1, int i2) {
		if (i1 < 0)
			i1 = 0;
		if (i2 < 0)
			i2 = 0;
		if (i1 > str.length())
			i1 = str.length();
		if (i2 > str.length())
			i2 = str.length();
		return str.substring(i1, i2);
	}

	public static String stripExtension(String str) {

		if (str == null)
			return null;

		int pos = str.lastIndexOf(".");

		if (pos == -1)
			return str;

		return str.substring(0, pos);
	}

	public static String escapeHTML(String str) {
		return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	
	public static String escape(String str) {
		return str.replaceAll("\b", "\\\\b").replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replaceAll("\f", "\\\\f").replaceAll("\r", "\\\\r");
	}
	
	public static String addSlashes(String str) {
		return str.replace("\\", "\\\\").replace("\"", "\\\"");
	}
	
	public static String stringFromBoolMap(HashMap<String, Object> m) {
		String s = "";
		Set<String> set = m.keySet();
		Iterator<String> setI = set.iterator();
		while(setI.hasNext()) {
			String key = setI.next();
			if(m.get(key) == Boolean.valueOf(true)) {
				s += key;
			}
		}
		return s.trim();
	}
	
	public static String getInitials(String s) {
		String initials = "";
		
		char lastChar = 0;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0) {
				initials += s.charAt(i);
			} else if(Character.isUpperCase(s.charAt(i))) {
				initials += s.charAt(i);
			} else if((lastChar == '_' || lastChar == '-') && Character.isAlphabetic(s.charAt(i))) {
				initials += s.charAt(i);
			}
			lastChar = s.charAt(i);
		}
		return initials.toUpperCase();
	}
}
