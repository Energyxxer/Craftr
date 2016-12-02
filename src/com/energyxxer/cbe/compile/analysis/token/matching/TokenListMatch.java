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
	protected boolean omittable = false;

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
		MethodInvocation thisInvoc = new MethodInvocation(this, "match", new String[] {"List<Token>"}, new Object[] {tokens});
		st.push(thisInvoc);
		if (tokens.size() == 0 && !omittable) {
			st.pop();
			return new TokenMatchResponse(false, null, 0, this.pattern, null);
		}
		boolean expectSeparator = false;

		boolean hasMatched = true;
		Token faultyToken = null;
		int length = 0;
		TokenPatternMatch expected = null;
		TokenList list = new TokenList().setName(this.name);

		for (int i = 0; i < tokens.size();) {

			if (this.separator != null && expectSeparator) {
				TokenMatchResponse itemMatch = this.separator.match(tokens.subList(i, tokens.size()), st);
				expectSeparator = false;
				if(!itemMatch.matched) {
					if(itemMatch.length > 0) {
						hasMatched = false;
						faultyToken = itemMatch.faultyToken;
						expected = itemMatch.expected;
						length += itemMatch.length;
						list.add(itemMatch.pattern);
						break;
					} else {
						break;
					}
				} else {
					i += itemMatch.length;
					length += itemMatch.length;
					list.add(itemMatch.pattern);
				}
			} else {
				if (this.separator != null) {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					if(!itemMatch.matched && (length > 0 || (length == 0 && !omittable))) {
						hasMatched = false;
						faultyToken = itemMatch.faultyToken;
						expected = itemMatch.expected;
						length += itemMatch.length;
						list.add(itemMatch.pattern);
						break;
					} else {
						i += itemMatch.length;
						length += itemMatch.length;
						list.add(itemMatch.pattern);
					}
				} else {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					length += itemMatch.length;
					if(!itemMatch.matched) {
						if((length > 0 || (length <= 0 && !omittable)) && itemMatch.length > 0) {
							hasMatched = false;
							faultyToken = itemMatch.faultyToken;
							expected = itemMatch.expected;
							list.add(itemMatch.pattern);
							break;

						} else {
							hasMatched = true;
							faultyToken = null;
							expected = null;
							break;
						}
					} else {
						i += itemMatch.length;
						list.add(itemMatch.pattern);
					}
				}
				expectSeparator = true;
			}
		}
		st.pop();
		return new TokenMatchResponse(hasMatched, faultyToken, length, expected, list);
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

	@Override
	public boolean isSensitive() {
		return true;
	}

	@Override
	public boolean isOmittable() {
		return omittable;
	}

	public TokenListMatch setOmittable(boolean value) {
		this.omittable = value;
		return this;
	}
}
