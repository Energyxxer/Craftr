package com.energyxxer.cbe.compile.analysis.token.structures;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.util.StringBounds;
import com.energyxxer.cbe.util.StringLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TokenPattern<T> {

	public String name = "";
	public abstract T getContents();
	public abstract TokenPattern<T> setName(String name);

	public abstract List<Token> search(String type);

	public abstract List<TokenPattern<?>> searchByName(String name);
	public abstract String flatten(boolean separate);

	public abstract File getFile();

	public String getLocation() {
		return getFile().toString() + ':' + getStringLocation();
	}

	public abstract StringLocation getStringLocation();
	public abstract StringBounds getStringBounds();
	public abstract int getCharLength();

	public String getFormattedPath() {
		StringLocation loc = getStringLocation();
		return "<a href=\"file:" + File.separator + File.separator + getFile() + "?" + loc.line + ":" + loc.column + "&" + getCharLength() + "\">"
				+ getLocation() + "</a>";
	}

	public abstract ArrayList<Token> flattenTokens();
}
