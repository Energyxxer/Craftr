package com.energyxxer.craftr.compile.parsing;

import com.energyxxer.craftr.compile.analysis.LangStructures;
import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenMatchResponse;
import com.energyxxer.craftr.compile.analysis.token.TokenStream;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenStructure;
import com.energyxxer.craftr.compile.exceptions.CraftrException;
import com.energyxxer.craftr.compile.exceptions.CraftrParserException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrFile;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrPackage;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrPackageManager;
import com.energyxxer.craftr.compile.parsing.classes.registries.UnitRegistry;
import com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.logic.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	private ArrayList<ArrayList<Token>> tokens = new ArrayList<>();
	private UnitRegistry reg = new UnitRegistry();

	public CraftrPackageManager packageManager = new CraftrPackageManager(new CraftrPackage("src"));

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
			Token fileHeader = f.get(0);
			if(!fileHeader.attributes.get("TYPE").equals("craftr")) continue;
			f.remove(0);

			TokenMatchResponse match = LangStructures.FILE.match(f);

			if(!match.matched) {
				Console.err.println(match.getFormattedErrorMessage());
				return;
			}

			TokenPattern<?> pattern = match.pattern;

			CraftrFile file;
			try {
				file = new CraftrFile(this, new File(f.get(0).file), pattern);
			} catch(CraftrParserException e) {
				Console.err.println(e.getMessage());
				return;
			}

			List<TokenPattern<?>> values = pattern.deepSearchByName("VALUE");
			for(TokenPattern<?> p : values) {
				if(p instanceof TokenStructure) {
					Console.debug.println(Evaluator.eval(p));
				} else {
					Console.warn.println("What.");
				}
			}

			List<TokenPattern<?>> units = pattern.searchByName("UNIT");

			for(TokenPattern<?> unit : units) {
				try {
					reg.add(new CraftrUnit(file, unit));
				} catch (CraftrException e) {
					Console.err.println(e.getMessage());
				}
			}
		}
	}
}
