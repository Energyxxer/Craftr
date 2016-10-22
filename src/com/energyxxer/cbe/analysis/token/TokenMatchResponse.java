package com.energyxxer.cbe.analysis.token;

public class TokenMatchResponse {
	public final boolean matched;
	public final Token faultyToken;
	public final int length;
	public String expected = null;

	public TokenMatchResponse(boolean matched, Token faultyToken, int length) {
		this.matched = matched;
		this.faultyToken = faultyToken;
		this.length = length;
	}

	public TokenMatchResponse(boolean matched, Token faultyToken, int length, String expected) {
		this.matched = matched;
		this.faultyToken = faultyToken;
		this.length = length;
		this.expected = expected;
	}

	@Override
	public String toString() {
		return "TokenMatchResponse [matched=" + matched + ", faultyToken=" + faultyToken + ", length=" + length
				+ ", expected=" + expected + "]";
	}

	public String getErrorMessage() {
		if (!matched)
			return "Uncaught Syntax Error: Unexpected token " + faultyToken.value + ". Expected " + expected
					+ ", instead got " + faultyToken.type + "\n\tat " + faultyToken.getLocation();
		return null;
	}

	public String getFormattedErrorMessage() {
		if (!matched)
			return "<span style=\"color:red;\">Uncaught Syntax Error: Unexpected token " + faultyToken.value
					+ ". Expected " + expected + ", instead got " + faultyToken.type + "\n\tat </span>"
					+ faultyToken.getFormattedPath() + "";
		return null;
	}
}
