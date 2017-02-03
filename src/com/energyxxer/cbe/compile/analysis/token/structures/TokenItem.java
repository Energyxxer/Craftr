package com.energyxxer.cbe.compile.analysis.token.structures;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.util.StringBounds;
import com.energyxxer.cbe.util.StringLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TokenItem extends TokenPattern<Token> {
	private Token token;
	
	public TokenItem(Token token) {
		this.token = token;
	}

	@Override
	public Token getContents() {
		return token;
	}
	
	@Override
	public TokenItem setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return "{ " + token + " }";
	}

	@Override
	public List<Token> search(String type) {
		ArrayList<Token> list = new ArrayList<>();
		if(token.type == type) list.add(token);
		return list;
	}

	@Override
	public List<Token> deepSearch(String type) {
		return search(type);
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		return new ArrayList<>();
	}

	@Override
	public List<TokenPattern<?>> deepSearchByName(String name) {
		return new ArrayList<>();
	}

	@Override
	public String flatten(boolean separate) {
		return token.value;
	}

	@Override
	public File getFile() {
		return new File(token.file);
	}

	@Override
	public StringLocation getStringLocation() {
		return new StringLocation(token.loc.index, token.loc.line, token.loc.column);
	}

	@Override
	public StringBounds getStringBounds() {
		return new StringBounds(
				new StringLocation(token.loc.index, token.loc.line, token.loc.column),
				new StringLocation(token.loc.index + token.value.length(), token.loc.line, token.loc.column + token.value.length())
		);
	}

    @Override
    public ArrayList<Token> flattenTokens() {
	    ArrayList<Token> list = new ArrayList<>();
	    list.add(token);
        return list;
    }

	@Override
	public String getType() {
		return "ITEM";
	}
}
