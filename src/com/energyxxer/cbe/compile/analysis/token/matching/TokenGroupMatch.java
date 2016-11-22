package com.energyxxer.cbe.compile.analysis.token.matching;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenGroup;
import com.energyxxer.cbe.util.MethodInvocation;
import com.energyxxer.cbe.util.Stack;
import com.energyxxer.cbe.util.StringUtil;

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

	@Override
	public int length() {
		return items.size();
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
		MethodInvocation thisInvoc = new MethodInvocation("TokenGroupMatch", "match", new String[] {"List<Token>"}, new Object[] {tokens});
		st.push(thisInvoc);
		/*if (tokens.size() == 0 && !optional)
			return new TokenMatchResponse(false, null, 0);*/
		TokenGroup group = (tokens.size() == 0) ? null : new TokenGroup().setName(this.name);
		int currentToken = 0;
		boolean hasMatched = true;
		Token faultyToken = null;
		int length = 0;
		TokenPatternMatch expected = null;
		for (int i = 0; i < items.size(); i++) {
			if (currentToken >= tokens.size() && !items.get(i).optional) {
				hasMatched = false;
				break;
			}

			TokenMatchResponse itemMatch = items.get(i).match(tokens.subList(currentToken, tokens.size()),st);
			if (!itemMatch.matched) {
				if(!items.get(i).optional) {
					hasMatched = false;
					faultyToken = itemMatch.faultyToken;
					expected = itemMatch.expected;
					break;
				}
			} else {
				group.add(itemMatch.pattern);
				currentToken += itemMatch.length;
				length += itemMatch.length;
			}
		}
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
		return StringUtil.escapeHTML(s);
	}
}
