package com.energyxxer.craftr.compile.analysis.token;

import com.energyxxer.craftr.util.StringLocation;
import com.energyxxer.craftr.util.StringUtil;

import java.io.File;
import java.util.HashMap;

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

	private static final boolean VERBOSE = false;

	public Token(String value, File file, StringLocation loc) {
		this.value = value;
		this.type = TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.loc = loc;
		this.attributes = new HashMap<>();
	}

	public Token(String value, String tokenType, File file, StringLocation loc) {
		this.value = value;
		this.type = (tokenType != null) ? tokenType : TokenType.getTypeOf(value);
		this.file = file.getAbsolutePath();
		this.filename = file.getName();
		this.loc = loc;
		this.attributes = new HashMap<>();
	}

	public boolean isSignificant() {
		return TokenType.isSignificant(type);
	}

	public String getLocation() {
		return filename + ":" + loc.line + ":" + loc.column + "#" + loc.index;
	}

	public String getFormattedPath() {
		return "\b" + file + "\b" + loc.index + "\b" + value.length() + "\b"
				+ getLocation() + "\b";
	}

	@Override
	public String toString() {
		return (!VERBOSE) ? value : getFullString();
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
		
		return getLocation() + o + type + o2 + value + attr + "\n";
	}
	
	public static Token merge(String type, Token... tokens) {
		String s = "";
		for(Token t : tokens) {
			s += t.value;
		}
		return new Token(s,type,new File(tokens[0].file),tokens[0].loc);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Token token = (Token) o;

		if (value != null ? !value.equals(token.value) : token.value != null) return false;
		if (type != null ? !type.equals(token.type) : token.type != null) return false;
		if (file != null ? !file.equals(token.file) : token.file != null) return false;
		if (loc != null ? !loc.equals(token.loc) : token.loc != null) return false;
		return attributes != null ? attributes.equals(token.attributes) : token.attributes == null;
	}

	@Override
	public int hashCode() {
		int result = value != null ? value.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (file != null ? file.hashCode() : 0);
		result = 31 * result + (loc != null ? loc.hashCode() : 0);
		result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
		return result;
	}
}
