package com.energyxxer.cbe.compile.analysis.token.matching;

import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenList;
import com.energyxxer.cbe.util.MethodInvocation;
import com.energyxxer.cbe.util.Stack;

import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.COMPLETE_MATCH;
import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.NO_MATCH;
import static com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse.PARTIAL_MATCH;

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
		MethodInvocation thisInvoc = new MethodInvocation(this, "match", new String[] {"List<Token>"}, new Object[] {tokens});
		st.push(thisInvoc);
		if (tokens.size() <= 0) {
			st.pop();
			return new TokenMatchResponse(false, null, 0, this.pattern, null);
		}
		boolean expectSeparator = false;

		boolean hasMatched = true;
		Token faultyToken = null;
		int length = 0;
		TokenPatternMatch expected = null;
		TokenList list = new TokenList().setName(this.name);

		itemLoop: for (int i = 0; i < tokens.size();) {

			if (this.separator != null && expectSeparator) {
				TokenMatchResponse itemMatch = this.separator.match(tokens.subList(i, tokens.size()), st);
				expectSeparator = false;
				switch(itemMatch.getMatchType()) {
					case NO_MATCH: {
						break itemLoop;
					}
					case PARTIAL_MATCH: {
						hasMatched = false;
						faultyToken = itemMatch.faultyToken;
						expected = itemMatch.expected;
						length += itemMatch.length;
						list.add(itemMatch.pattern);
						break itemLoop;
					}
					case COMPLETE_MATCH: {
						i += itemMatch.length;
						length += itemMatch.length;
						list.add(itemMatch.pattern);
					}
				}
			} else {
				if (this.separator != null) {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					switch(itemMatch.getMatchType()) {
						case NO_MATCH: {
							if(length <= 0) {
								hasMatched = false;
								faultyToken = itemMatch.faultyToken;
								expected = itemMatch.expected;
								length += itemMatch.length;
								list.add(itemMatch.pattern);
								break itemLoop;
							} else {
								break itemLoop;
							}
						}
						case PARTIAL_MATCH: {
							hasMatched = false;
							faultyToken = itemMatch.faultyToken;
							expected = itemMatch.expected;
							length += itemMatch.length;
							list.add(itemMatch.pattern);
							break itemLoop;
						}
						case COMPLETE_MATCH: {
							i += itemMatch.length;
							length += itemMatch.length;
							list.add(itemMatch.pattern);
							expectSeparator = true;
							continue;
						}
					}
				} else {
					TokenMatchResponse itemMatch = this.pattern.match(tokens.subList(i, tokens.size()), st);
					length += itemMatch.length;
					switch(itemMatch.getMatchType()) {
						case NO_MATCH: {
							if(length <= 0) {
								hasMatched = false;
								faultyToken = itemMatch.faultyToken;
								expected = itemMatch.expected;
								length += itemMatch.length;
								list.add(itemMatch.pattern);
								break itemLoop;
							} else {
								break itemLoop;
							}
						}
						case PARTIAL_MATCH: {
							hasMatched = false;
							faultyToken = itemMatch.faultyToken;
							expected = itemMatch.expected;
							list.add(itemMatch.pattern);
							break itemLoop;
						}
						case COMPLETE_MATCH: {
							i += itemMatch.length;
							list.add(itemMatch.pattern);
							continue;
						}
					}
				}
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
}
