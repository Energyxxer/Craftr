package com.energyxxer.craftr.compile.analysis;

import com.energyxxer.craftr.compile.analysis.presets.CraftrAnalysisProfile;
import com.energyxxer.craftr.compile.analysis.presets.JSONAnalysisProfile;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContext;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContextResponse;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisProfile;
import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenStream;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.util.StringLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * For tokenizing any file by rules given by Analyzer Profiles
 */
public class Analyzer {
	
	private TokenStream stream;
	
	public Analyzer(File project, TokenStream stream) {
		this.stream = stream;
		parse(project);
	}

	public Analyzer(File file, String str, TokenStream stream) {
		this.stream = stream;
		AnalysisProfile profile = null;
		if(file.getName().endsWith(".craftr")) profile = new CraftrAnalysisProfile();
		else if(file.getName().endsWith(".json")) profile = new JSONAnalysisProfile();
		if(profile != null) tokenize(file, str, profile);
	}
	
	private void parse(File project) {
		if (!project.exists())
			return;
		File[] files = project.listFiles();
		if(files == null) return;
		for (File file : files) {
			String name = file.getName();
			if (file.isDirectory()) {
				if(!file.getName().equals("resources") || !file.getParentFile().getParent().equals(Preferences.get("workspace_dir"))) {
					//This is not the resource pack directory.
				}
				parse(file);
			} else if(name.endsWith(".craftr")) {
				try {
					String str = new String(Files.readAllBytes(Paths.get(file.getPath())));
					tokenize(file, str, new CraftrAnalysisProfile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(name.endsWith(".json")) {
				try {
					String str = new String(Files.readAllBytes(Paths.get(file.getPath())));
					tokenize(file, str, new JSONAnalysisProfile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private File file;

	private StringBuilder token = new StringBuilder("");
	private int line = 0;
	private int column = 0;
	private int index = 0;

	private String tokenType = null;

	private int tokenLine = 0;
	private int tokenColumn = 0;
	private int tokenIndex = 0;

	private void tokenize(File file, String str, AnalysisProfile profile) {
		this.file = file;
		stream.setProfile(profile);
		profile.setStream(stream);
		line = column = index = tokenLine = tokenColumn = tokenIndex = 0;
		token.setLength(0);

		{
			Token header = new Token("", TokenType.FILE_HEADER, file, new StringLocation(0, 0, 0));
			profile.putHeaderInfo(header);
			flush(header);
		}

		mainLoop: for(int i = 0; i <= str.length(); i++) {
			this.index = i;
			String c = "";

			boolean isClosingIteration = true;

			if(i < str.length()) {
				c = Character.toString(str.charAt(i));
				isClosingIteration = false;
			}

			String sub = str.substring(i);

			if (c.equals("\n")) {
				line++;
				column = 0;
			} else {
				column++;
			}

			for(AnalysisContext ctx : profile.contexts) {
				AnalysisContextResponse response = ctx.analyze(sub);
				if(response.success) {
					flush();
					updateTokenPos();
					line += response.endLocation.line;
					column += response.endLocation.column;
					i += response.value.length()-1;
					token.append(response.value);
					tokenType = response.tokenType;
					flush();
					continue mainLoop;
				}
			}

			if(isClosingIteration) {
				flush();
				break;
			}

			if(Character.isWhitespace(c.charAt(0))) {
				//Is whitespace.
				flush();
				continue;
			} else if(token.length() == 0) {
				//Is start of a new token.
				updateTokenPos();
				tokenType = null;
			}

			char lastChar = '\u0000';

			if(i > 0) lastChar = str.charAt(i-1);

			if(lastChar != '\u0000' && !profile.canMerge(lastChar,c.charAt(0))) {
				flush();
				updateTokenPos();
			}
			token.append(c);
		}
		flush();

		updateTokenPos();
		token.setLength(0);
		tokenType = TokenType.END_OF_FILE;
		flush();
	}

	private void updateTokenPos() {
		tokenLine = line;
		tokenColumn = column;
		tokenIndex = index;
	}

	private void flush() {
		if(token.length() > 0 || (tokenType == TokenType.FILE_HEADER || tokenType == TokenType.END_OF_FILE))
			flush(new Token(token.toString(), tokenType, file, new StringLocation(tokenIndex, tokenLine, tokenColumn)));

		token.setLength(0);
		tokenType = null;
	}
	
	private void flush(Token token) {
		stream.write(token);
	}
}
