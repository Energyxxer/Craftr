package com.energyxxer.cbe.compile.analysis.token.structures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.util.StringLocation;

public class TokenStructure extends TokenPattern<TokenPattern<?>> {
	private TokenPattern<?> group;
	
	public TokenStructure(String name, TokenPattern<?> group) {
		this.name = name;
		this.group = group;
	}

	@Override
	public TokenPattern<?> getContents() {
		return group;
	}
	
	@Override
	public TokenStructure setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return group.toString();
	}

	@Override
	public List<Token> search(String type) {
		return group.search(type);
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<TokenPattern<?>>();
		if(this.name.equals(name)) list.add(this);
		list.addAll(group.searchByName(name));
		return list;
	}

	@Override
	public String flatten(boolean separate) {
		return group.flatten(separate);
	}

	@Override
	public File getFile() {
		return group.getFile();
	}

	@Override
	protected StringLocation getStringLocation() {
		return group.getStringLocation();
	}

	@Override
	protected int getCharLength() {
		return group.getCharLength();
	}

}
