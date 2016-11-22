package com.energyxxer.cbe.compile.analysis.token.structures;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;

public class TokenList extends TokenPattern<TokenPattern<?>[]> {
	private ArrayList<TokenPattern<?>> patterns = new ArrayList<TokenPattern<?>>();
	
	public TokenList() {}
	
	public TokenList(ArrayList<TokenPattern<?>> patterns) {
		this.patterns = patterns;
	}
	
	public void add(TokenPattern<?> pattern) {
		patterns.add(pattern);
	}
	
	@Override
	public TokenList setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TokenPattern<?>[] getContents() {
		return patterns.toArray(new TokenPattern<?>[0]);
	}

	@Override
	public String toString() {
		String o = "{ ";
		
		for(TokenPattern<?> p : patterns) {
			o += p.toString();
		}
		o += " }";
		return o;
	}

	@Override
	public List<Token> search(String type) {
		ArrayList<Token> list = new ArrayList<Token>();
		for(TokenPattern<?> p : patterns) {
			list.addAll(p.search(type));
		}
		return list;
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<TokenPattern<?>>();
		if(this.name.equals(name)) list.add(this);
		for(TokenPattern<?> p : patterns) {
			list.addAll(p.searchByName(name));
		}
		return list;
	}

}
