package com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.util.Stack;

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