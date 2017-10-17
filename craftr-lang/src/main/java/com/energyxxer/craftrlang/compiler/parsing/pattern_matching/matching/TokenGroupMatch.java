package com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.util.MethodInvocation;
import com.energyxxer.util.Stack;

import java.util.ArrayList;
import java.util.List;

import static com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse.COMPLETE_MATCH;
import static com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse.NO_MATCH;
import static com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse.PARTIAL_MATCH;

/**
 * Represents a group of token items. This represents a structure a sequence of
 * tokens should meet to match a given token structure.
 */
public class TokenGroupMatch extends TokenPatternMatch {
	public ArrayList<TokenPatternMatch> items;

	public TokenGroupMatch() {
		this.optional = false;
		items = new ArrayList<>();
	}

	public TokenGroupMatch(boolean optional) {
		this.optional = optional;
		items = new ArrayList<>();
	}

	public TokenGroupMatch append(TokenPatternMatch i) {
		items.add(i);
		return this; //I'm so sorry for this
	}
	
	public TokenMatchResponse match(List<Token> tokens) {
		return match(tokens,new Stack());
	}
	
	@Override
	public TokenGroupMatch setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TokenMatchResponse match(List<Token> tokens, Stack st) {
		if(items.size() == 0) return new TokenMatchResponse(true, null, 0, null, new TokenGroup());

		MethodInvocation thisInvoc = new MethodInvocation(this, "match", new String[] {"List<Token>"}, new Object[] {tokens});

		if(tokens.size() <= 0 || st.find(thisInvoc)) {
			return new TokenMatchResponse(false, null, 0, this, null);
		}

		st.push(thisInvoc);

		TokenGroup group = (tokens.size() == 0) ? null : new TokenGroup().setName(this.name).addTags(this.tags);
		int currentToken = 0;
		boolean hasMatched = true;
		Token faultyToken = null;
		int length = 0;
		TokenPatternMatch expected = null;
		itemLoop: for (int i = 0; i < items.size(); i++) {

			if (currentToken >= tokens.size() && !items.get(i).optional) {
				hasMatched = false;
				expected = items.get(i);
				break;
			}

			List<Token> subList = tokens.subList(currentToken, tokens.size());

			TokenMatchResponse itemMatch = items.get(i).match(subList,st);
			switch(itemMatch.getMatchType()) {
				case NO_MATCH: {
					if(!items.get(i).optional) {
						hasMatched = false;
						faultyToken = itemMatch.faultyToken;
						expected = itemMatch.expected;
						break itemLoop;
					}
					break;
				}
				case PARTIAL_MATCH: {
					if(!(items.get(i).optional && i+1 < items.size() && items.get(i+1).match(subList,st).matched)) {
						hasMatched = false;
						length += itemMatch.length;
						faultyToken = itemMatch.faultyToken;
						expected = itemMatch.expected;
						break itemLoop;
					} else {
						continue itemLoop;
					}
				}
				case COMPLETE_MATCH: {
					group.add(itemMatch.pattern);
					currentToken += itemMatch.length;
					length += itemMatch.length;
				}
			}
		}
		st.pop();
		return new TokenMatchResponse(hasMatched, faultyToken, length, expected, group);
	}

	@Override
	public String toString() {
		String s = "";
		if (this.optional) {
			s += "[";
		} else {
			s += "<";
		}
		for (int i = 0; i < items.size(); i++) {
			s += items.get(i);
			if (i < items.size() - 1) {
				s += " ";
			}
		}
		if (this.optional) {
			s += "]";
		} else {
			s += ">";
		}
		return s;
	}

	@Override
	public String deepToString(int levels) {
		if(levels <= 0) return toString();
		String s = "";
		if (this.optional) {
			s += "[";
		} else {
			s += "<";
		}
		for (int i = 0; i < items.size(); i++) {
			s += items.get(i).deepToString(levels-1);
			if (i < items.size() - 1) {
				s += " ";
			}
		}
		if (this.optional) {
			s += "]";
		} else {
			s += ">";
		}
		return s;
	}

	@Override
	public String toTrimmedString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			sb.append(items.get(i).toTrimmedString());
			if (i < items.size() - 1) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}
}
