package com.energyxxer.cbe.analysis.token.structures;

import java.util.List;

import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenMatchResponse;

/**
 * Represents a condition a single token should meet for it to be considered
 * matching to a token structure.
 */
public class TokenItem {
	protected String type;
	protected String stringMatch = null;
	protected final boolean optional;

	public TokenItem(String type) {
		this.type = type;
		this.optional = false;
	}

	public TokenItem(String type, String stringMatch) {
		this.type = type;
		this.stringMatch = stringMatch;
		this.optional = false;
	}

	public TokenItem(String type, boolean optional) {
		this.type = type;
		this.optional = optional;
	}

	public TokenItem(String type, String stringMatch, boolean optional) {
		this.type = type;
		this.stringMatch = stringMatch;
		this.optional = optional;
	}

	public TokenMatchResponse match(List<Token> tokens) {

		boolean matched;
		Token faultyToken = null;
		int length = length();

		if (tokens.size() == 0) {
			matched = false;
			length = 0;
		}
		if (stringMatch != null) {
			matched = tokens.get(0).type.equals(this.type) && tokens.get(0).value.equals(stringMatch);
		} else {
			matched = tokens.get(0).type.equals(this.type);
		}

		if (!matched && tokens.size() > 0) {
			faultyToken = tokens.get(0);
		}

		if (!matched && optional) {
			matched = true;
			faultyToken = null;
			length = 0;
		}

		return new TokenMatchResponse(matched, faultyToken, length, (matched) ? null : type);
	}

	public int length() {
		return 1;
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
		if (stringMatch != null) {
			s += ":" + stringMatch;
		}
		if (optional) {
			s += ">";
		} else {
			s += "]";
		}
		return s;
	}
}
