package com.energyxxer.cbe.compile.analysis.token;

import com.energyxxer.cbe.compile.analysis.token.matching.TokenPatternMatch;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.global.Commons;
import com.energyxxer.cbe.util.ColorUtil;
import com.energyxxer.cbe.util.StringUtil;

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
		String ft = null;
		if(faultyToken != null) ft = StringUtil.escapeHTML(faultyToken.value);
		String e = null;
		if(expected != null) e = StringUtil.escapeHTML(expected.toTrimmedString());
		return "TokenMatchResponse [matched=" + matched + ", faultyToken=" + ft + ", length=" + length
				+ ", expected=" + e + "]";
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
				return "<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">Uncaught Syntax Error: Unexpected end of input. Expected "
						+ expected + ".\n</span>";
			}
			if(expected == null) {
				return "<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">Uncaught Syntax Error: Unexpected token " + faultyToken.value
						+ ".\n\tat </span>"
						+ faultyToken.getFormattedPath() + "";
			}
			if(faultyToken != null && faultyToken.type == TokenType.END_OF_FILE) {
				return "<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">Uncaught Syntax Error: Unexpected end of input. Expected "
						+ StringUtil.escapeHTML(expected.toTrimmedString()) + ".\n\tat </span>"
						+ faultyToken.getFormattedPath() + "";
			}
			return "<span style=\"color:" + ColorUtil.toCSS(Commons.errorColor) + ";\">Uncaught Syntax Error: Unexpected token " + faultyToken.value
					+ ". Expected " + StringUtil.escapeHTML(expected.toTrimmedString())
					+ ", instead got " + faultyToken.type + "\n\tat </span>"
					+ faultyToken.getFormattedPath() + "";
		}
		return null;
	}
}
