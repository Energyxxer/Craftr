package com.energyxxer.craftr.compiler.parsing.pattern_matching.matching;

import java.util.List;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftr.util.MethodInvocation;
import com.energyxxer.craftr.util.Stack;

/**
 * Represents a condition a single token should meet for it to be considered
 * matching to a token structure.
 */
public class TokenItemMatch extends TokenPatternMatch {
	protected String type;
	protected String stringMatch = null;

	public TokenItemMatch(String type) {
		this.type = type;
		this.optional = false;
	}

	public TokenItemMatch(String type, String stringMatch) {
		this.type = type;
		this.stringMatch = stringMatch;
		this.optional = false;
	}

	public TokenItemMatch(String type, boolean optional) {
		this.type = type;
		this.optional = optional;
	}

	public TokenItemMatch(String type, String stringMatch, boolean optional) {
		this.type = type;
		this.stringMatch = stringMatch;
		this.optional = optional;
	}
	
	public TokenMatchResponse match(List<Token> tokens) {
		return match(tokens,new Stack());
	}
	
	@Override
	public TokenItemMatch setName(String name) {
		this.name = name;
		return this;
	}

	public TokenMatchResponse match(List<Token> tokens, Stack st) {
		MethodInvocation thisInvoc = new MethodInvocation(this, "match", new String[] {"List<Token>"}, new Object[] {tokens});
		st.push(thisInvoc);
		boolean matched;
		Token faultyToken = null;

		if (tokens.size() == 0) {
			matched = false;
		} else if (stringMatch != null) {
			matched = tokens.get(0).type.equals(this.type) && tokens.get(0).value.equals(stringMatch);
		} else {
			matched = tokens.get(0).type.equals(this.type);
		}

		if (!matched && tokens.size() > 0) {
			faultyToken = tokens.get(0);
		}

		int length = (matched) ? 1 : 0;
		
		TokenItem item = null;
		if(tokens.size() > 0) item = new TokenItem(tokens.get(0)).setName(this.name);

		st.pop();
		return new TokenMatchResponse(matched, faultyToken, length, (matched) ? null : this, item);
	}
	
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		String s = "";
		if (optional) {
			s += "[";
		} else {
			s += "<";
		}
		s += type;
		if (stringMatch != null) {
			s += ":" + stringMatch;
		}
		if (optional) {
			s += "]";
		} else {
			s += ">";
		}
		return s;
	}

	@Override
	public String deepToString(int levels) {
		return toString();
	}

	@Override
	public String toTrimmedString() {
		return (stringMatch != null) ? stringMatch : type;
	}
}
