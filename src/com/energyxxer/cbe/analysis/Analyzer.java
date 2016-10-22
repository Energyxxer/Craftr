package com.energyxxer.cbe.analysis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.energyxxer.cbe.Window;
import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenAttributes;
import com.energyxxer.cbe.analysis.token.TokenStream;
import com.energyxxer.cbe.analysis.token.TokenType;
import com.energyxxer.util.StringLocation;

/**
 * For tokenizing CBE files. At the moment, all tokens go through the static
 * method tokenStream, though it will later be replaced with a variable argument
 * to which to send tokens.
 */
public class Analyzer {
	
	private TokenStream stream;
	
	public Analyzer(File project, TokenStream stream) {
		this.stream = stream;
		parse(project);
	};
	
	public Analyzer(File file, String str, TokenStream stream) {
		this.stream = stream;
		tokenize(file, str);
	};
	
	public void parse(File project) {
		if (!project.exists())
			return;
		File[] files = project.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				parse(files[i]);
			} else {
				try {
					byte[] encoded = Files.readAllBytes(Paths.get(files[i].getPath()));
					String s = new String(encoded);
					tokenize(files[i], s);
				} catch (IOException e) {
					e.printStackTrace(new PrintWriter(Window.consoleout));
				}
			}
		}
	}

	public void tokenize(File file, String str) {
		//str = str.replaceAll("\r\n", "\n").replaceAll(Matcher.quoteReplacement("\t"), "    ");

		String token = null;
		int line = 0;
		int column = 0;
		String tokenType = null;
		char lastChar = '\u0000';
		boolean alphanumeric = false;
		boolean isComment = false;
		boolean isString = false;
		boolean isNumber = false;

		int cLine = 1;
		int cColumn = 0;
		
		int tokenIndex = 0;
		
		String c = "";

		mainLoop: for (int i = 0; i < str.length(); 
				alphanumeric = Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0,
				lastChar = c.charAt(0),
				
				i++
			) {
			c = "" + str.charAt(i);

			if (c.equals("\n")) {
				cLine++;
				cColumn = 0;
			} else {
				cColumn++;
			}

			// CHECK FOR SPECIAL CASES FIRST
			if (!isString && Arrays.asList(LangConstants.stringLiteral).indexOf(c) >= 0) {
				if (token != null)
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));

				line = cLine;
				column = cColumn;

				token = c;
				tokenIndex = i;
				tokenType = TokenType.STRING_LITERAL;
				isString = true;
				continue;
			} else if (isString) {
				if (c.equals("" + '\\')) {
					token += c;
					token += str.charAt(i + 1);
					i++;
					continue;
				} else if (Arrays.asList(LangConstants.stringLiteral).indexOf(c) >= 0) {
					token += c;
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					token = tokenType = null;
					isString = false;
					continue;
				} else {
					token += c;
					continue;
				}
			}
			if (str.substring(i).startsWith(LangConstants.comment[0])) {
				if (token != null)
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
				token = null;
				tokenType = TokenType.COMMENT;
				if (str.substring(i).indexOf("\n") >= 0) {
					token = str.substring(i, i + str.substring(i).indexOf("\n"));
					tokenIndex = i;

					line = cLine;
					column = cColumn;

					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					token = tokenType = null;

					cLine++;
					cColumn = 0;

					i += str.substring(i).indexOf("\n");
					continue;
				} else {
					token = str.substring(i);
					tokenIndex = i;

					line = cLine;
					column = cColumn;

					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					token = tokenType = null;
					return;
				}
			}
			if (!isComment && str.substring(i).startsWith(LangConstants.multilinecomment[0])) {
				isComment = true;
				if (token != null)
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));

				line = cLine;
				column = cColumn;

				token = c;
				tokenIndex = i;
				tokenType = TokenType.COMMENT;
				continue;
			} else if (isComment) {
				if (str.substring(i).startsWith(LangConstants.multilinecomment[1])) {
					isComment = false;
					token += LangConstants.multilinecomment[1];
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					i += LangConstants.multilinecomment[1].length() - 1;
					token = tokenType = null;
				} else {
					token += c;
				}
				continue;
			}
			for (int j = 0; j < LangConstants.operators.length; j++) {
				if (str.substring(i).startsWith(LangConstants.operators[j])) {
					if (token != null)
						flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					tokenType = TokenType.OPERATOR;

					line = cLine;
					column = cColumn;

					flush(new Token(LangConstants.operators[j], tokenType, file, new StringLocation(i, line, column)));
					token = tokenType = null;
					i += LangConstants.operators[j].length() - 1;
					continue mainLoop;
				}
			}
			if(Arrays.asList(LangConstants.annotations).indexOf(c) >= 0) {
				if (token != null)
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
				tokenType = TokenType.ANNOTATION;

				line = cLine;
				column = cColumn;

				flush(new Token(c, tokenType, file, new StringLocation(i, line, column)));
				token = tokenType = null;
				continue;
			} else if (Arrays.asList(LangConstants.end_of_statement).indexOf(c) >= 0) {
				if (token != null)
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
				tokenType = TokenType.END_OF_STATEMENT;

				line = cLine;
				column = cColumn;

				flush(new Token(c, tokenType, file, new StringLocation(i, line, column)));
				token = tokenType = null;
				continue;
			}
			if (Character.isDigit(c.charAt(0)) && !Character.isJavaIdentifierPart(lastChar)) {
				if (!isNumber && token != null) {
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
					token = tokenType = null;
				}
				isNumber = true;
				tokenType = TokenType.NUMBER;
				if(token == null) tokenIndex = i;
				token = (token != null) ? token + c : c;

				line = cLine;
				column = cColumn;

				continue;
			} else if (isNumber && Arrays.asList(LangConstants.numberPunctuation).indexOf(c) >= 0) {
				token += c;
				continue;
			} else if (isNumber && !Character.isJavaIdentifierPart(c.charAt(0))) {
				if (token != null) {
					flush(new Token(token, tokenType, file, new StringLocation(tokenIndex, line, column)));
				}
				token = tokenType = null;
				isNumber = false;
			}
			
			// CONTINUE CHECKING

			if (Character.isWhitespace(c.charAt(0))) {
				if (token != null) {
					flush(new Token(token, file, new StringLocation(tokenIndex, line, column)));
				}
				token = tokenType = null;
			} else if (token == null) {

				line = cLine;
				column = cColumn;

				token = c;
				tokenIndex = i;
			} else if (alphanumeric == (Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0)) {
				token += c;
			} else if (alphanumeric != (Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0)) {
				flush(new Token(token, file, new StringLocation(tokenIndex, line, column)));
				token = c;
				tokenIndex = i;

				line = cLine;
				column = cColumn;

			}
			if (i >= str.length() - 1) {
				if (token != null)
					flush(new Token(token, file, new StringLocation(tokenIndex, line, column)));
		
				flush(new Token("", TokenType.END_OF_FILE, file, new StringLocation(tokenIndex, cLine, cColumn+1)));
			}
		}
	}
	
	public void flush(Token token) {
		if (token == null)
			return;
		
		//token = new Token(token.value, token.type, new File(token.file), new StringLocation(token.loc.index - (token.value.length()), token.loc.line, token.loc.column));
		
		if(token.type == TokenType.NUMBER) {
			try {
				Double.parseDouble(token.value);
			} catch(NumberFormatException e) {
				token.type = TokenType.getTypeOf(token.value);
			}
		}
		if(stream.tokens.size() >= 1) {
			if(token.type == TokenType.IDENTIFIER && Arrays.asList(LangConstants.unit_types).indexOf(token.value) >= 0 && stream.tokens.get(stream.tokens.size()-1).type == TokenType.QUALIFIER)
				token.type = TokenType.UNIT_TYPE;
		} else {
			if(token.type == TokenType.IDENTIFIER && Arrays.asList(LangConstants.unit_types).indexOf(token.value) >= 0)
				token.type = TokenType.UNIT_TYPE;
		}
		
		TokenAttributes.giveAttributes(token);
		stream.write(token);
	}
}
