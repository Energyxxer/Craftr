package com.energyxxer.cbe.analysis.token.structures;

import java.util.List;

import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenMatchResponse;

/**
 * Represents a sequence of tokens of a given type, of an undefined length.
 */
public class TokenList extends TokenItem {
	protected String separator = null;

	public TokenList(String type) {
		super(type);
	}

	public TokenList(String type, String separator) {
		super(type);
		this.separator = separator;
	}

	public TokenList(String type, boolean optional) {
		super(type, optional);
	}

	public TokenList(String type, String separator, boolean optional) {
		super(type, optional);
		this.separator = separator;
	}

	@Override
	public TokenMatchResponse match(List<Token> tokens) {
		if (tokens.size() == 0) {
			return new TokenMatchResponse(false, null, 0, this.type);
		}
		if (!tokens.get(0).type.equals(this.type)) {
			return new TokenMatchResponse(false, tokens.get(0), 0, this.type);
		}
		boolean expectSeparator = true;
		int length = 0;
		for (int i = 1; i < tokens.size(); i++) {
			length++;
			if (this.separator != null && expectSeparator) {
				if (!tokens.get(i).type.equals(this.separator)) {
					return new TokenMatchResponse(true, null, length);
				}
				expectSeparator = false;
			} else {
				if (this.separator != null) {
					if (!tokens.get(i).type.equals(this.type)) {
						return new TokenMatchResponse(false, tokens.get(i), length, this.type);
					}
				} else {
					if (!tokens.get(i).type.equals(this.type)) {
						return new TokenMatchResponse(true, null, length);
					}
				}
				expectSeparator = true;
			}
		}
		return new TokenMatchResponse(true, null, length);
	}

	@Override
	public int length() {
		return -1;
	}

	@Override
	public String toString() {
		String s = "";
		if (optional) {
			s += "<";
		} else {
			s += "[";
		}
		s += type;
		if (separator != null) {
			s += "," + separator;
		}
		s += "...";
		if (optional) {
			s += ">";
		} else {
			s += "]";
		}
		return s;
	}
}
