package com.energyxxer.cbe.analysis.token;

import java.io.File;

/**
 * Class containing a value, or token, its type, source file and location within it.
 * */
public class Token {
	public final String value;
	public final String type;
	public final String file;
	public final String filename;
	public final int line;
	public final int column;

	public Token(String value, File file, int line, int column) {
		this.value = value;
		this.type=TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.line = line;
		this.column = column;
	}
	public Token(String value, String tokenType, File file, int line, int column) {
		this.value = value;
		this.type=(tokenType != null) ? tokenType : TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.line = line;
		this.column = column;
	}
	public boolean isSignificant() {
		return TokenType.isSignificant(type);
	}
	
	public String getLocation() {
		return filename + ":" + line + ":" + column;
	}
	
	public String getFormattedPath() {
		return "<a href=\"file:" + File.separator + File.separator + file + "?" + line + ":" + column + "\">" + getLocation() + "</a>";
	}
	
	@SuppressWarnings("unused")
	@Override
	public String toString() {
		if(true) return value;
		int extraSpaces = 32 - getLocation().length();
		String o = "";
		while(o.length() < extraSpaces) {
			o+=" ";
		}
		int extraSpaces2 = "END_OF_STATEMENT".length() - type.length() + 4;
		String o2 = "";
		while(o2.length() < extraSpaces2) {
			o2+=" ";
		}
		//return "Token#" + type + ":  " + o + value;
		return getLocation() + o + type + o2 +  value;
	}
}
