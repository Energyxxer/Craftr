package com.energyxxer.cbe.compile.analysis.token.matching;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenGroup;
import com.energyxxer.cbe.util.MethodInvocation;
import com.energyxxer.cbe.util.Stack;
import com.energyxxer.cbe.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.COMPLETE_MATCH;
import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.NO_MATCH;
import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.PARTIAL_MATCH;

/**
 * Represents a group of token items. This represents a structure a sequence of
 * tokens should meet to match a given token structure.
 */
public class TokenGroupMatch extends TokenPatternMatch {
	public ArrayList<TokenPatternMatch> items;

	public TokenGroupMatch() {
		this.optional = false;
		items = new ArrayList<TokenPatternMatch>();
	}

	public TokenGroupMatch(boolean optional) {
		this.optional = optional;
		items = new ArrayList<TokenPatternMatch>();
	}

	public TokenGroupMatch append(TokenPatternMatch i) {
		items.add(i);
		return this;
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
		st.push(thisInvoc);

		TokenGroup group = (tokens.size() == 0) ? null : new TokenGroup().setName(this.name);
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

			TokenMatchResponse itemMatch = items.get(i).match(tokens.subList(currentToken, tokens.size()),st);
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
					hasMatched = false;
					length += itemMatch.length;
					faultyToken = itemMatch.faultyToken;
					expected = itemMatch.expected;
					break itemLoop;
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
		String s = "";
		for (int i = 0; i < items.size(); i++) {
			s += items.get(i).toTrimmedString();
			if (i < items.size() - 1) {
				s += " ";
			}
		}
		return s;
	}
}
