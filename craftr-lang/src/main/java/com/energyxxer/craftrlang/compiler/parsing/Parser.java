package com.energyxxer.craftrlang.compiler.parsing;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	
	private ArrayList<ArrayList<Token>> files = new ArrayList<>();

	private HashMap<File, TokenPattern<?>> filePatterns = new HashMap<>();

	private ArrayList<Notice> notices = new ArrayList<>();

	public Parser(Compiler compiler, TokenStream ts) {

		ArrayList<Token> currentList = new ArrayList<>();
		for(Token t : ts.tokens) {
			currentList.add(t);
			if(t.type == TokenType.END_OF_FILE) {
				ArrayList<Token> f = new ArrayList<>();
				f.addAll(currentList);
				files.add(f);
				currentList.clear();
			}
		}

		for(ArrayList<Token> f : files) {
			Token fileHeader = f.get(0);
			if(!fileHeader.attributes.get("TYPE").equals("craftr")) continue;
			f.remove(0);

			TokenMatchResponse match = CraftrProductions.FILE.match(f);

			if(!match.matched) {
				notices.add(new Notice(NoticeType.ERROR, match.getErrorMessage(), match.faultyToken.getFormattedPath()));
				continue;
			}

			TokenPattern<?> pattern = match.pattern;

			filePatterns.put(new File(f.get(0).file), pattern);
		}
	}

	public ArrayList<ArrayList<Token>> getFiles() {
		return files;
	}

	public HashMap<File, TokenPattern<?>> getFilePatterns() {
		return filePatterns;
	}

	public ArrayList<Notice> getNotices() {
		return notices;
	}
}
