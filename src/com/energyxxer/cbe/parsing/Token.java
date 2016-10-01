package com.energyxxer.cbe.parsing;

/**
 * Class containing a value, or token, its type, source file and location within it.
 * */
public class Token {
	public final String value;
	public final String type;
	public final String file;
	public final int line;
	public final int column;

	public Token(String value, String file, int line, int column) {
		this.value = value;
		this.type=TokenType.getTypeOf(value);
		this.file = file;
		this.line = line;
		this.column = column;
	}
	public Token(String value, String tokenType, String file, int line, int column) {
		this.value = value;
		this.type=(tokenType != null) ? tokenType : TokenType.getTypeOf(value);
		this.file = file;
		this.line = line;
		this.column = column;
	}
	public boolean isSignificant() {
		return TokenType.isSignificant(type);
	}

	@Override
	public String toString() {
		int extraSpaces = "END_OF_STATEMENT".length() - type.length();
		String o = "";
		while(o.length() < extraSpaces) {
			o+=" ";
		}
		//return "Token#" + type + ":  " + o + value;
		return file + ":" + line + ":" + column + "    " + value;
	}
}
