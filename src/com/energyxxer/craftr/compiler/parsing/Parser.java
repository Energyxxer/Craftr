package com.energyxxer.craftr.compiler.parsing;

import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftr.global.Console;

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
				Console.err.println(match.getFormattedErrorMessage());
				Console.warn.println(match.pattern);
				Console.debug.println(match);
				return;
			}

			TokenPattern<?> pattern = match.pattern;

			filePatterns.put(new File(f.get(0).file), pattern);
		}
	}
}
