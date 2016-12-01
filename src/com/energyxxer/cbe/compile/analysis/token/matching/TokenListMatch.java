package com.energyxxer.cbe.compile.analysis.token.matching;

import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenList;
import com.energyxxer.cbe.util.MethodInvocation;
import com.energyxxer.cbe.util.Stack;

/**
 * Represents a sequence of tokens of a given type, of an undefined length.
 */
public class TokenListMatch extends TokenPatternMatch {
	protected TokenPatternMatch pattern;
	protected TokenPatternMatch separator = null;

	public TokenListMatch(String type) {
		this.pattern = new TokenItemMatch(type);
		this.optional = false;
	}

	public TokenListMatch(String type, String separator) {
		this.pattern = new TokenItemMatch(type);
		this.optional = false;
		this.separator = new TokenItemMatch(separator);
	}

	public TokenListMatch(String type, boolean optional) {
		this.pattern = new TokenItemMatch(type);
		this.optional = optional;
	}

	public TokenListMatch(String type, String separator, boolean optional) {
		this.pattern = new TokenItemMatch(type);
		this.optional = optional;
		this.separator = new TokenItemMatch(separator);
	}
	
	public TokenListMatch(TokenPatternMatch type) {
		this.pattern = type;
		this.optional = false;
	}

	public TokenListMatch(TokenPatternMatch type, TokenPatternMatch separator) {
		this.pattern = type;
		this.optional = false;
		this.separator = separator;
	}

	public TokenListMatch(TokenPatternMatch type, boolean optional) {
		this.pattern = type;
		this.optional = optional;
	}

	public TokenListMatch(TokenPatternMatch type, TokenPatternMatch separator, boolean optional) {
		this.pattern = type;
		this.optional = optional;
		this.separator = separator;
	}
	
	@Override
	public TokenListMatch setName(String name) {
		this.name = name;
		return this;
	}
	
	public TokenMatchResponse match(List<Token> tokens) {
		return match(tokens,new Stack());
	}

	@Override
	public TokenMatchResponse match(List<Token> tokens, Stack st) {
		MethodInvocation thisInvoc = new MethodInvocation("TokenListMatch", "match", new String[] {"List<Token>"}, new Object[] {tokens});
		st.push(thisInvoc);
		if (tokens.size() == 0) {
			return new TokenMatchResponse(false, null, 0, this.pattern, null);
		}
		boolean expectSeparator = false;
		int length = 0;
		TokenList list = new TokenList().setName(this.name);
		for (int i = 0; i < tokens.size(); i++) {
			if (this.separator != null && expectSeparator) {
				TokenMatchResponse itemMatch = this.separator.match(tokens.subList(i, tokens.size()), st);
				if(!itemMatch.matched) {
					return new TokenMatchResponse(true, null, length, list);
				} else {
					list.add(itemMatch.pattern);
					i += itemMatch.length-1;
					length += itemMatch.length-1;
				}
				expectSeparator = false;
			} else {
				if (this.separator != null) {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					if(!itemMatch.matched) {
						return new TokenMatchResponse(false, tokens.get(i), length, this.pattern, list);
					} else {
						list.add(itemMatch.pattern);
						i += itemMatch.length-1;
						length += itemMatch.length-1;
					}
				} else {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					if(!itemMatch.matched) {
						if(length > 0) {
							return new TokenMatchResponse(true, null, length, list);
						} else {
							return new TokenMatchResponse(false, itemMatch.faultyToken, length, itemMatch.expected, list);
						}
					} else {
						list.add(itemMatch.pattern);
						i += itemMatch.length-1;
						length += itemMatch.length-1;
					}
				}
				expectSeparator = true;
			}
			length++;
		}
		return new TokenMatchResponse(true, null, length, list);
	}

	@Override
	public int length() {
		return -1;
	}

	@Override
	public String toString() {
		String s = "";
		if (optional) {
			s += "[";
		} else {
			s += "<";
		}
		s += pattern;
		if (separator != null) {
			s += "," + separator;
		}
		s += "...";
		if (optional) {
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
		if (optional) {
			s += "[";
		} else {
			s += "<";
		}
		s += pattern.deepToString(levels-1);
		if (separator != null) {
			s += "," + separator;
		}
		s += "...";
		if (optional) {
			s += "]";
		} else {
			s += ">";
		}
		return s;
	}

	@Override
	public String toTrimmedString() {
		String s = "";
		s += pattern.toTrimmedString();
		if (separator != null) {
			s += "," + separator.toTrimmedString();
		}
		s += "...";
		return s;
	}
}
