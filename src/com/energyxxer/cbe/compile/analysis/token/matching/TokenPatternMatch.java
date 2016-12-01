package com.energyxxer.cbe.compile.analysis.token.matching;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.util.Stack;
import com.energyxxer.cbe.util.StringUtil;

import java.util.List;

public abstract class TokenPatternMatch {
	public String name = "";
	public boolean optional;
	
	public abstract TokenMatchResponse match(List<Token> tokens);

	public abstract TokenMatchResponse match(List<Token> tokens, Stack st);
	
	public TokenPatternMatch setName(String name) {
		this.name = name;
		return this;
	}

	public abstract String deepToString(int levels);

	public abstract String toTrimmedString();
	
}