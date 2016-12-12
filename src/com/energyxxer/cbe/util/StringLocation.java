package com.energyxxer.cbe.util;

public class StringLocation {
	public int index;
	public int line;
	public int column;
	
	public StringLocation(int index, int line, int column) {
		super();
		this.index = index;
		this.line = line;
		this.column = column;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(line).append(':').append(column).append('#').append(index).toString();
	}
}
