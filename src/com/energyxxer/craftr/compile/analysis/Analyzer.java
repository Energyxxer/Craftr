package com.energyxxer.craftr.compile.analysis;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenAttributes;
import com.energyxxer.craftr.compile.analysis.token.TokenStream;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.global.Commons;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.util.StringLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.energyxxer.craftr.compile.analysis.Analyzer.Context.COMMENT;
import static com.energyxxer.craftr.compile.analysis.Analyzer.Context.DEFAULT;
import static com.energyxxer.craftr.compile.analysis.Analyzer.Context.MULTI_LINE_COMMENT;
import static com.energyxxer.craftr.compile.analysis.Analyzer.Context.NUMBER;
import static com.energyxxer.craftr.compile.analysis.Analyzer.Context.STRING;

/**
 * For tokenizing Craftr files. At the moment, all tokens go through the static
 * method tokenStream, though it will later be replaced with a variable argument
 * to which to send tokens.
 */
public class Analyzer {
	
	private TokenStream stream;
	
	public Analyzer(File project, TokenStream stream) {
		this.stream = stream;
		parse(project);
	}

	public Analyzer(File file, String str, TokenStream stream) {
		this.stream = stream;
		tokenize(file, str);
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
					parse(file);
				}
			} else if(name.endsWith(".craftr")) {
				try {
					String str = new String(Files.readAllBytes(Paths.get(file.getPath())));
					tokenize(file, str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	enum Context {
		DEFAULT, STRING, NUMBER, COMMENT, MULTI_LINE_COMMENT
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

	private Context context = DEFAULT;
	private String contextData = "";

	private void tokenize(File file, String str) {
		this.file = file;
		line = column = index = tokenLine = tokenColumn = tokenIndex = 0;

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

			if(context == STRING) {
				//Is inside a string.

				if(c.equals("\n")) {
					//Is a line end.
					if(contextData.equals(LangConstants.multi_line_string_literal[0])) {
						//Is a multiline string.
						token.append("\n");
						continue;
					} else {
						//Invalid end of string. Flush anyways.
						//TODO: Add a way to tell the caller a syntax error occurs here
						flush();
						continue;
					}
				} else if(c.equals("\\")) {
					//Is an escaped character.
					//Add backslash.
					token.append("\\");
					if(!isClosingIteration && !Commons.isSpecialCharacter(str.charAt(i+1))) {
						//Add escaped character.
						token.append(str.charAt(i+1));
						//Skip scanning next character.
						i++;
					}
					continue;
				} else if(sub.startsWith(contextData)) {
					//Found same delimiter that started the string literal. Close the string.
					token.append(c);
					i += contextData.length()-1;
					flush();
					continue;
				} else {
					//Is a character inside the string.
					token.append(c);
					continue;
				}
			} else if(context == COMMENT) {
				//Is inside a single-line comment.

				if(c.equals("\n")) {
					//Found the end of the comment.
					flush();
					continue;
				} else {
					//Is a character inside the comment.
					token.append(c);
					continue;
				}
			} else if(context == MULTI_LINE_COMMENT) {
				//Is inside a multi-line comment.

				if(!isClosingIteration && sub.startsWith(LangConstants.multi_line_comment[1])) {
					//Found the end of the multi-line comment.
					token.append(LangConstants.multi_line_comment[1]);
					i += LangConstants.multi_line_comment[1].length()-1;
					flush();
					continue;
				} else {
					//Is a character inside the comment.
					token.append(c);
					continue;
				}
			} else if(context == NUMBER) {
				//Is inside a number.

				if(isClosingIteration) {
					//Found the end of the file.
					flush();
					continue;
				} else if(Character.isDigit(c.charAt(0))) {
					//Is a digit.
					token.append(c);
				} else if(sub.startsWith(LangConstants.number_punctuation[0])) {
					//Is a dot.
					if(contextData.equals("false")) {
						//Valid dot. Accounts for a floating point literal.
						token.append(LangConstants.number_punctuation[0]);
						i += LangConstants.number_punctuation[0].length() - 1;
						contextData = "true";
						continue;
					} else {
						//Dot is not part of the number, and treated as a normal dot token.
						flush();
						//Do not go to the next iteration; let the fallback handle this.
					}
				} else {
					//Is a letter or symbol.
					for(String suffix : LangConstants.number_type_suffix) {
						if(sub.startsWith(suffix)) {
							//Is a valid numerical suffix.
							token.append(suffix);
							flush();
							i += suffix.length()-1;
							continue mainLoop;
						}
					}
					//Is not a valid suffix.
					flush();
					//Do not go to the next iteration; let the fallback handle this.
				}
			}

			//--------------------
			//  FALLBACK CONTEXT
			//--------------------

			//Check for context changes.

			//String literals.
			for(String delimiter : LangConstants.string_literal) {
				if(sub.startsWith(delimiter)) {
					token.append(delimiter);
					context = STRING;
					contextData = delimiter;
					tokenType = TokenType.STRING_LITERAL;
					updateTokenPos();
					i += delimiter.length()-1;
					continue mainLoop;
				}
			}
			//Comments.
			for(String delimiter : LangConstants.comment) {
				if(sub.startsWith(delimiter)) {
					token.append(delimiter);
					context = COMMENT;
					contextData = "";
					tokenType = TokenType.COMMENT;
					updateTokenPos();
					i += delimiter.length()-1;
					continue mainLoop;
				}
			}
			//Multi-line comments.
			{
				String start = LangConstants.multi_line_comment[0];
				if(sub.startsWith(start)) {
					token.append(start);
					context = MULTI_LINE_COMMENT;
					contextData = "";
					tokenType = TokenType.COMMENT;
					updateTokenPos();
					i += start.length() - 1;
					continue;
				}
			}

			//Check for special case tokens.

			for(SpecialTokenConstant special : LangConstants.specialConstants) {
				for(String pattern : special.patterns) {
					if(sub.startsWith(pattern)) {
						//Is pattern.

						//Flush any previous tokens.
						flush();

						//Flush new pattern.
						token.append(pattern);
						tokenType = special.type;
						updateTokenPos();
						flush();

						//Continue main loop.
						i += pattern.length()-1;
						continue mainLoop;
					}
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

			if(Character.isJavaIdentifierPart(c.charAt(0))) {
				if(!Character.isJavaIdentifierPart(lastChar)) {
					flush();
					updateTokenPos();
				}
				token.append(c);
			} else {
				flush();

				updateTokenPos();
				token.append(c);
				flush();
			}
		}
		flush();

		updateTokenPos();
		token.setLength(0);
		tokenType = TokenType.END_OF_FILE;
		flush();

		//stream.write(new Token("", TokenType.END_OF_FILE, file, new StringLocation(index, line, column)));

	}

	private void updateTokenPos() {
		tokenLine = line;
		tokenColumn = column;
		tokenIndex = index;
	}

	private void flush() {
		if(token.length() > 0 || tokenType == TokenType.END_OF_FILE)
			flush(new Token(token.toString(), tokenType, file, new StringLocation(tokenIndex, tokenLine, tokenColumn)));

		context = DEFAULT;
		contextData = "";
		token.setLength(0);
		tokenType = null;
	}
	
	private void flush(Token token) {
		if(stream.tokens.size() >= 1) {
			if(token.type == TokenType.IDENTIFIER && Arrays.asList(LangConstants.unit_types).indexOf(token.value) >= 0 && stream.tokens.get(stream.tokens.size()-1).type == TokenType.MODIFIER)
				token.type = TokenType.UNIT_TYPE;
		} else {
			if(token.type == TokenType.IDENTIFIER && Arrays.asList(LangConstants.unit_types).indexOf(token.value) >= 0)
				token.type = TokenType.UNIT_TYPE;
		}
		
		TokenAttributes.giveAttributes(token);
		stream.write(token);
	}
}
