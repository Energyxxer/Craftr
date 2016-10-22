package com.energyxxer.cbe.analysis.token.structures;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenMatchResponse;

/**
 * Represents a group of token items. This represents a structure a sequence of
 * tokens should meet to match a given token structure.
 */
public class TokenGroup extends TokenItem {
	public ArrayList<TokenItem> items;

	public TokenGroup() {
		super(null);
		items = new ArrayList<TokenItem>();
	}

	public TokenGroup(boolean optional) {
		super(null, optional);
		items = new ArrayList<TokenItem>();
	}

	@Override
	public int length() {
		return items.size();
	}

	public void append(TokenItem i) {
		items.add(i);
	}

	@Override
	public TokenMatchResponse match(List<Token> tokens) {
		if (optional)
			return new TokenMatchResponse(true, null, length());
		if (tokens.size() == 0 && !optional)
			return new TokenMatchResponse(false, null, 0);
		int currentToken = 0;
		boolean hasMatched = true;
		Token faultyToken = null;
		int length = 0;
		String expected = null;
		for (int i = 0; i < items.size(); i++) {
			if (currentToken >= tokens.size() && !items.get(i).optional) {
				hasMatched = false;
				break;
			}

			TokenMatchResponse itemMatch = items.get(i).match(tokens.subList(currentToken, tokens.size()));
			if (!itemMatch.matched) {
				hasMatched = false;
				faultyToken = itemMatch.faultyToken;
				length += itemMatch.length;
				expected = itemMatch.expected;
				break;
			} else {
				currentToken += itemMatch.length;
				length += itemMatch.length;
			}
		}
		return new TokenMatchResponse(hasMatched, faultyToken, length, expected);
	}

	@Override
	public String toString() {
		String s = "";
		if (optional) {
			s += "<";
		} else {
			s += "[";
		}
		for (int i = 0; i < items.size(); i++) {
			s += items.get(i);
			if (i < items.size() - 1) {
				s += " ";
			}
		}
		if (optional) {
			s += ">";
		} else {
			s += "]";
		}
		return s;
	}
}
