package com.energyxxer.cbe.compile.analysis.token.structures;

import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;

public abstract class TokenPattern<T> {

	public String name = "";
	public abstract T getContents();
	
	public abstract TokenPattern<T> setName(String name);

	public abstract List<Token> search(String type);
	public abstract List<TokenPattern<?>> searchByName(String name);
}
