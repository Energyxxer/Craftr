package com.energyxxer.cbe.analysis.token;

import java.io.File;
import java.util.HashMap;

import com.energyxxer.util.StringLocation;
import com.energyxxer.util.StringUtil;

/**
 * Class containing a value, or token, its type, source file and location within
 * it.
 */
public class Token {
	public String value;
	public String type;
	public String file;
	public String filename;
	public StringLocation loc;
	public HashMap<String, Object> attributes;

	public Token(String value, File file, StringLocation loc) {
		this.value = value;
		this.type = TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.loc = loc;
		this.attributes = new HashMap<String, Object>();
	}

	public Token(String value, String tokenType, File file, StringLocation loc) {
		this.value = value;
		this.type = (tokenType != null) ? tokenType : TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.loc = loc;
		this.attributes = new HashMap<String, Object>();
	}

	public boolean isSignificant() {
		return TokenType.isSignificant(type);
	}

	public String getLocation() {
		return filename + ":" + loc.line + ":" + loc.column + "#" + loc.index;
	}

	public String getFormattedPath() {
		return "<a href=\"file:" + File.separator + File.separator + file + "?" + loc.line + ":" + loc.column + "\">"
				+ getLocation() + "</a>";
	}

	@Override
	public String toString() {
		return StringUtil.escapeHTML(value);
	}

	public String getFullString() {
		int extraSpaces = 32 - getLocation().length();
		String o = "";
		while (o.length() < extraSpaces) {
			o += " ";
		}
		int extraSpaces2 = "END_OF_STATEMENT".length() - type.length() + 4;
		String o2 = "";
		while (o2.length() < extraSpaces2) {
			o2 += " ";
		}
		// return "Token#" + type + ": " + o + value;
		
		String attr = StringUtil.stringFromBoolMap(attributes);
		if(attr.length() > 0) {
			attr = "\t#" + attr;
		}
		
		return getLocation() + o + type + o2 + StringUtil.escapeHTML(value) + attr;
	}
}
