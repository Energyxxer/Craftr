package com.energyxxer.cbe.compile.parsing;

import com.energyxxer.cbe.compile.analysis.LangStructures;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEFile;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEPackage;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEPackageManager;
import com.energyxxer.cbe.compile.parsing.classes.registries.UnitRegistry;
import com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit;
import com.energyxxer.cbe.compile.parsing.exceptions.CBEParserException;
import com.energyxxer.cbe.global.Console;
import com.energyxxer.cbe.logic.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	private ArrayList<ArrayList<Token>> tokens = new ArrayList<>();
	private UnitRegistry reg = new UnitRegistry();

	public CBEPackageManager packageManager = new CBEPackageManager(new CBEPackage("src"));

	public Parser(TokenStream ts, Project project) {
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

			TokenMatchResponse match = LangStructures.FILE.match(f);

			if(!match.matched) {
				Console.err.println(match.getFormattedErrorMessage());
				return;
			}

			TokenPattern<?> pattern = match.pattern;

			CBEFile file;
			try {
				file = new CBEFile(this, new File(f.get(0).file), pattern);
			} catch(CBEParserException e) {
				Console.err.println(e.getMessage());
				return;
			}

			List<TokenPattern<?>> units = pattern.searchByName("UNIT");

			for(TokenPattern<?> unit : units) {
				try {
					reg.add(new CBEUnit(file, unit));
				} catch (CBEParserException e) {
					Console.err.println(e.getMessage());
				}
				/*String type = unit.search(TokenType.UNIT_TYPE).get(0).value;
				Token nameToken = ((TokenItem) pattern.searchByName("UNIT_NAME").get(0)).getContents();
				String name = nameToken.value;
				String file = nameToken.file;

				List<TokenPattern<?>> actions = pattern.searchByName("UNIT_ACTION");

				if (type.equals("entity")) {
					CBEEntity e = new CBEEntity(name, project, new File(file)).setDeclaration(pattern);
					for (TokenPattern<?> action : actions) {
						if (action.search(TokenType.UNIT_ACTION).get(0).value.equals("extends")) {
							e.entityExtends = action.search(TokenType.IDENTIFIER).get(0).value;
						}
					}

					reg.add(e);
				}*/
			}
		}
		/*if(!reg.close()) return;
		
		for(CBEEntity e : reg) {
			ProjectManager.setIconFor(e.file, "*entities" + File.separator + e.entityType);
		}
		System.out.println(reg);*/
	}
}
