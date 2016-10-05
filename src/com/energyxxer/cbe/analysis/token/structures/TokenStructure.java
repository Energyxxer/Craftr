package com.energyxxer.cbe.analysis.token.structures;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenMatchResponse;

/**
 * Represents a token structure, containing multiple ways a
 * series of tokens could match with this structure.
 * */
public class TokenStructure {
	public ArrayList<TokenGroup> entries = new ArrayList<TokenGroup>();
	
	public void add(TokenGroup g) {
		entries.add(g);
	}
	
	public TokenMatchResponse match(List<Token> tokens) {
		int maxLength = 0;
		for(int i = 0; i < entries.size(); i++) {
			TokenMatchResponse itemMatch = entries.get(i).match(tokens);
			if(!itemMatch.matched) return new TokenMatchResponse(false, itemMatch.faultyToken, itemMatch.length, itemMatch.expected);
			maxLength = Math.max(maxLength, itemMatch.length);
		}
		return new TokenMatchResponse(true, null, maxLength);
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < entries.size(); i++) {
			s += entries.get(i);
			if(i < entries.size()-1) {
				s += "\n OR";
			}
		}
		return s;
	}
}
