package com.energyxxer.craftr.compiler.lexical_analysis.token;

import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.matching.TokenPatternMatch;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;

import java.util.ArrayList;
import java.util.Iterator;

public class TokenStream implements Iterable<Token> {
	
	public ArrayList<Token> tokens = new ArrayList<>();

	private boolean includeInsignificantTokens = false;

	private ScannerProfile profile = null;

	public TokenStream() {includeInsignificantTokens = false;}
	public TokenStream(boolean includeInsignificantTokens) {
		this.includeInsignificantTokens = includeInsignificantTokens;
	}
	
	public final void write(Token token) {
		write(token, false);
	}
	
	public final void write(Token token, boolean skip) {
		if(skip || (profile == null || !profile.filter(token))) {
			onWrite(token);
			if(profile == null || (includeInsignificantTokens || profile.isSignificant(token)))
				tokens.add(token);
		}
	}

	public void setProfile(ScannerProfile profile) {
		this.profile = profile;
	}

	public void onWrite(Token token) {}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}

	public ArrayList<TokenPattern<?>> search(TokenPatternMatch m) {

	    ArrayList<TokenPattern<?>> matches = new ArrayList<>();

	    for(int i = 0; i < tokens.size(); i++) {
	        TokenMatchResponse response = m.match(tokens.subList(i,tokens.size()));
	        if(response.matched) {
	            matches.add(response.pattern);
	            i += response.length-1;
            }
        }

        return matches;
    }

	@Override
	public String toString() {
		return "TokenStream{" +
				"tokens=" + tokens +
				", includeInsignificantTokens=" + includeInsignificantTokens +
				'}';
	}
}
