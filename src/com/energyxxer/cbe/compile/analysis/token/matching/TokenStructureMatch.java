package com.energyxxer.cbe.compile.analysis.token.matching;

import java.util.ArrayList;
import java.util.List;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenStructure;
import com.energyxxer.cbe.util.MethodInvocation;
import com.energyxxer.cbe.util.Stack;

/**
 * Represents a token structure, containing multiple ways a series of tokens
 * could match with this structure.
 */
public class TokenStructureMatch extends TokenPatternMatch {
	public ArrayList<TokenPatternMatch> entries = new ArrayList<TokenPatternMatch>();
	
	public TokenStructureMatch(String name) {
		this.name = name;
		optional = false;
	}
	
	public TokenStructureMatch(String name, boolean optional) {
		this.name = name;
		this.optional = optional;
	}

	public TokenStructureMatch add(TokenPatternMatch g) {
		entries.add(g);
		return this;
	}
	
	public TokenMatchResponse match(List<Token> tokens) {
		return match(tokens,new Stack());
	}
	
	@Override
	public TokenStructureMatch setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TokenMatchResponse match(List<Token> tokens, Stack st) {
		MethodInvocation thisInvoc = new MethodInvocation(("TokenStructureMatch" + "#" + name).intern(), "match", new String[] {"List<Token>"}, new Object[] {tokens});
		if(st.find(thisInvoc)) {
			//System.out.println("Breaking infinite loop...");
			return new TokenMatchResponse(false, tokens.get(0), 1, null);
		}
		st.push(thisInvoc);
		
		TokenMatchResponse longestMatch = null;
		for (int i = 0; i < entries.size(); i++) {
			TokenMatchResponse itemMatch = entries.get(i).match(tokens, st);
			
			if(longestMatch == null) {
				longestMatch = itemMatch;
			} else if(itemMatch.length >= longestMatch.length) {
				if(longestMatch.matched && !itemMatch.matched) {
					
				} else {
					longestMatch = itemMatch;
				}
			}
			
		}
		if(longestMatch == null || longestMatch.matched) {			
			return new TokenMatchResponse(true, null, (longestMatch == null) ? 0 : longestMatch.length, (longestMatch == null) ? null : new TokenStructure(this.name,longestMatch.pattern));
		} else {
			return new TokenMatchResponse(false, longestMatch.faultyToken, longestMatch.length, longestMatch.expected, new TokenStructure(this.name,longestMatch.pattern));
		}
	}
	
	@Override
	public int length() {
		return -1;
	}

	@Override
	public String toString() {
		return ((optional) ? "[" : "<") + "-" + name + "-" + ((optional) ? "]" : ">");
	}
	
	public String deepToString() {
		String s = "";
		for (int i = 0; i < entries.size(); i++) {
			if(entries.get(i) instanceof TokenStructureMatch) {
				s += "[-" + ((TokenStructureMatch) (entries.get(i))).name + "-]";
			} else {
				s += entries.get(i);
			}
			if (i < entries.size() - 1) {
				s += "\n OR";
			}
		}
		return s;
	}
}
