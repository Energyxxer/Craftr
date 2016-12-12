package com.energyxxer.cbe.compile.analysis.token.structures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.util.StringLocation;

public class TokenGroup extends TokenPattern<TokenPattern<?>[]> {
	private ArrayList<TokenPattern<?>> patterns = new ArrayList<TokenPattern<?>>();
	
	public TokenGroup() {}
	
	public TokenGroup(ArrayList<TokenPattern<?>> patterns) {
		this.patterns = patterns;
	}
	
	public void add(TokenPattern<?> pattern) {
		patterns.add(pattern);
	}

	@Override
	public TokenPattern<?>[] getContents() {
		return patterns.toArray(new TokenPattern<?>[0]);
	}
	
	@Override
	public TokenGroup setName(String name) {
		this.name = name;
		return this;
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

	@Override
	public String flatten(boolean separate) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < patterns.size(); i++) {
			sb.append(patterns.get(i).flatten(separate));
			if(i < patterns.size()-1 && separate) sb.append(" ");
		}
		return sb.toString();
	}

	@Override
	public File getFile() {
		return (patterns != null && patterns.size() > 0) ? patterns.get(0).getFile() : null;
	}

	@Override
	protected StringLocation getStringLocation() {
		if (patterns == null || patterns.size() <= 0) return null;
		StringLocation l = null;
		for (TokenPattern<?> pattern : patterns) {
			StringLocation loc = pattern.getStringLocation();
			if(l == null) {
				l = loc;
				continue;
			}
			if(loc.index < l.index) {
				l = loc;
				continue;
			}
		}
		return l;
	}

	@Override
	protected int getCharLength() {
		int l = 0;
		for(TokenPattern<?> pattern : patterns) {
			l += pattern.getCharLength();
		}
		return l;
	}

}
