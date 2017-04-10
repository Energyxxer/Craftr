package com.energyxxer.craftrlang.compiler.parsing;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	
	private ArrayList<ArrayList<Token>> tokens = new ArrayList<>();

	public HashMap<File, TokenPattern<?>> filePatterns = new HashMap<>();

	public Parser(TokenStream ts) {

		ArrayList<Token> currentList = new ArrayList<>();
		for(Token t : ts.tokens) {
			currentList.add(t);
			if(t.type == TokenType.END_OF_FILE) {
				ArrayList<Token> f = new ArrayList<>();
				for(Token t2 : currentList) {
					f.add(t2);
				}
				tokens.add(f);
				currentList.clear();
			}
		}


		for(ArrayList<Token> f : tokens) {
			Token fileHeader = f.get(0);
			if(!fileHeader.attributes.get("TYPE").equals("craftr")) continue;
			f.remove(0);

			TokenMatchResponse match = CraftrStructures.FILE.match(f);

			if(!match.matched) {
				System.err.println(match.getFormattedErrorMessage());
				System.out.println(match.pattern);
				System.out.println(match);
				return;
			}

			TokenPattern<?> pattern = match.pattern;

			filePatterns.put(new File(f.get(0).file), pattern);
		}
	}
}
