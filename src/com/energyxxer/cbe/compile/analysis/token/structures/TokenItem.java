package com.energyxxer.cbe.compile.analysis.token.structures;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;

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
		ArrayList<Token> list = new ArrayList<Token>();
		if(token.type == type) list.add(token);
		return list;
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<TokenPattern<?>>();
		if(this.name.equals(name)) list.add(this);
		return list;
	}

}
