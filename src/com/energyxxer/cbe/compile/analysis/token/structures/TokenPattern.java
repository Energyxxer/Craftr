package com.energyxxer.cbe.compile.analysis.token.structures;

import java.io.File;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.util.Range;
import com.energyxxer.cbe.util.StringLocation;

public abstract class TokenPattern<T> {

	public String name = "";
	public abstract T getContents();
	public abstract TokenPattern<T> setName(String name);

	public abstract List<Token> search(String type);

	public abstract List<TokenPattern<?>> searchByName(String name);
	public abstract String flatten(boolean separate);

	public abstract File getFile();

	private String getLocation() {
		return new StringBuilder().append(getFile()).append(':').append(getStringLocation()).toString();
	}

	protected abstract StringLocation getStringLocation();
	protected abstract int getCharLength();

	public String getFormattedPath() {
		StringLocation loc = getStringLocation();
		return "<a href=\"file:" + File.separator + File.separator + getFile() + "?" + loc.line + ":" + loc.column + "&" + getCharLength() + "\">"
				+ getLocation() + "</a>";
	}
}
