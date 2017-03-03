package com.energyxxer.craftr.compiler.parsing.pattern_matching;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.matching.TokenPatternMatch;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.util.StringUtil;

public class TokenMatchResponse {
	public final boolean matched;
	public final Token faultyToken;
	public final int length;
	public TokenPatternMatch expected = null;
	public TokenPattern<?> pattern = null;

	public static final int NO_MATCH = 0;
	public static final int PARTIAL_MATCH = 1;
	public static final int COMPLETE_MATCH = 2;

	public TokenMatchResponse(boolean matched, Token faultyToken, int length, TokenPattern<?> pattern) {
		this.matched = matched;
		this.faultyToken = faultyToken;
		this.length = length;
		this.pattern = pattern;
	}
	
	public TokenMatchResponse(boolean matched, Token faultyToken, int length, TokenPatternMatch expected, TokenPattern<?> pattern) {
		this.matched = matched;
		this.faultyToken = faultyToken;
		this.length = length;
		this.expected = expected;
		this.pattern = pattern;
	}

	public int getMatchType() {
		if(matched) return COMPLETE_MATCH;
		if(length > 0) return PARTIAL_MATCH;
		return NO_MATCH;
	}

	@Override
	public String toString() {
		return "TokenMatchResponse{" +
				"matched=" + matched +
				", faultyToken=" + faultyToken +
				", length=" + length +
				", expected=" + expected +
				", pattern=" + pattern +
				", matchType=" + getMatchType() +
				'}';
	}

	public String getErrorMessage() {
		if (!matched) {
			if(faultyToken == null) {
				return "Uncaught Syntax Error: Unexpected end of input. Expected " + expected + ".\n";
			}
			if(faultyToken.type == TokenType.END_OF_FILE) {
				return "Uncaught Syntax Error: Unexpected end of input. Expected " + expected + ".\n\tat " + faultyToken.getLocation();
			}
			return "Uncaught Syntax Error: Unexpected token " + faultyToken.value + ". Expected " + expected
					+ ", instead got " + faultyToken.type + "\n\tat " + faultyToken.getLocation();
		}
		return null;
	}

	public String getFormattedErrorMessage() {
		if (!matched) {
			if(faultyToken == null) {
				return "Uncaught Syntax Error: Unexpected end of input. Expected "
						+ expected + ".\n";
			}
			if(expected == null) {
				return "Uncaught Syntax Error: Unexpected token " + faultyToken.value
						+ ".\n\tat "
						+ faultyToken.getFormattedPath() + "";
			}
			if(faultyToken.type == TokenType.END_OF_FILE) {
				return "Uncaught Syntax Error: Unexpected end of input. Expected "
						+ StringUtil.escapeHTML(expected.toTrimmedString()) + ".\n\tat "
						+ faultyToken.getFormattedPath() + "";
			}
			return "Uncaught Syntax Error: Unexpected token " + faultyToken.value
					+ ". Expected " + StringUtil.escapeHTML(expected.toTrimmedString())
					+ ", instead got " + faultyToken.type + "\n\tat "
					+ faultyToken.getFormattedPath() + "";
		}
		return null;
	}
}
