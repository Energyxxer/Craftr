package com.energyxxer.util;

/**
 * Things for strings.
 * */
public class StringUtil {
	public static String ellipsis(String str, int max) {
		if(str.length() > max) {
			return (str.substring(0, max-3) + "...").intern();
		} else {
			return str;
		}
	}
	
	public static String substring(String str,int i1,int i2) {
		if(i1 < 0) i1=0;
		if(i2 < 0) i2=0;
		if(i1 > str.length()) i1 = str.length();
		if(i2 > str.length()) i2 = str.length();
		return str.substring(i1,i2);
	}
	
	public static String stripExtension(String str) {

        if (str == null) return null;

        int pos = str.lastIndexOf(".");

        if (pos == -1) return str;

        return str.substring(0, pos);
    }
	
	public static String escapeHTML(String str) {
		return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
