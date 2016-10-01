package com.energyxxer.cbe.parsing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.energyxxer.cbe.Window;

/**
 * For tokenizing CBE files. At the moment, all tokens go
 * through the static method tokenStream, though it will later be
 * replaced with a variable argument to which to send tokens.
 * */
public class CBEParser {
	public static void parse(File project) {
		if(!project.exists() || !project.isDirectory()) return;
		File[] files = project.listFiles();
		for(int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				parse(files[i]);
			} else {
				try {
					byte[] encoded = Files.readAllBytes(Paths.get(files[i].getPath()));
					String s = new String(encoded);
					tokenize(files[i].getName(),s);
				} catch (IOException e) {
					e.printStackTrace(new PrintWriter(Window.consoleout));
				}
			}
		}
	}
	public static void tokenize(String filename, String str) {
		str = str.replaceAll("\r\n", "\n");
		//System.out.println(str.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\t", "\\\\t"));
		
		String token = null;
		int line = 0;
		int column = 0;
		String tokenType = null;
		boolean alphanumeric = false;
		boolean isComment = false;
		boolean isString = false;
		boolean isNumber = false;
		
		int cLine = 1;
		int cColumn = 0;
		
		mainLoop: for(int i = 0; i < str.length(); i++) {
			String c = "" + str.charAt(i);
			
			if(c.equals("\n")) {
				cLine++;
				cColumn = 0;
			} else {
				cColumn++;
			}
			
			//CHECK FOR SPECIAL CASES FIRST
			if(!isString && Arrays.asList(LangConstants.stringLiteral).indexOf(c) >= 0) {
				if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
				
				line = cLine;
				column = cColumn;
				
				token = "" + c;
				tokenType = TokenType.STRING_LITERAL;
				isString = true;
				continue;
			} else if(isString) {
				if(c.equals("" + '\\')) {
					token += c;
					token += str.charAt(i+1);
					i++;
					continue;
				} else if(Arrays.asList(LangConstants.stringLiteral).indexOf(c) >= 0) {
					token += c;
					tokenStream(new Token(token,tokenType,filename,line,column));
					token = tokenType = null;
					isString = false;
					continue;
				} else {
					token += c;
					continue;
				}
			}
			if(str.substring(i).startsWith(LangConstants.comment[0])) {
				if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
				token = null;
				tokenType = TokenType.COMMENT;
				if(str.substring(i).indexOf("\n") >= 0) {
					token = str.substring(i, i + str.substring(i).indexOf("\n"));
					
					line = cLine;
					column = cColumn;
					
					tokenStream(new Token(token,tokenType,filename,line,column));
					token = tokenType = null;
					
					cLine++;
					cColumn = 0;
					
					i += str.substring(i).indexOf("\n");
					continue;
				} else {
					token = str.substring(i);
					
					line = cLine;
					column = cColumn;
					
					tokenStream(new Token(token,tokenType,filename,line,column));
					token = tokenType = null;
					return;
				}
			}
			if(!isComment && str.substring(i).startsWith(LangConstants.multilinecomment[0])) {
				isComment = true;
				if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
				
				line = cLine;
				column = cColumn;
				
				token = c;
				tokenType = TokenType.COMMENT;
				continue;
			} else if(isComment) {
				if(str.substring(i).startsWith(LangConstants.multilinecomment[1])) {
					isComment = false;
					token += LangConstants.multilinecomment[1];
					tokenStream(new Token(token,tokenType,filename,line,column));
					i += LangConstants.multilinecomment[1].length()-1;
					token = tokenType = null;
				} else {
					token += c;
				}
				continue;
			}
			for(int j = 0; j < LangConstants.operators.length; j++) {
				if(str.substring(i).startsWith(LangConstants.operators[j])) {
					if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
					tokenType = TokenType.OPERATOR;
					
					line = cLine;
					column = cColumn;
					
					tokenStream(new Token(LangConstants.operators[j],tokenType,filename,line,column));
					token = tokenType = null;
					i += LangConstants.operators[j].length() - 1;
					continue mainLoop;
				}
			}
			if(Arrays.asList(LangConstants.end_of_statement).indexOf(c) >= 0) {
				if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
				tokenType = TokenType.END_OF_STATEMENT;
				
				line = cLine;
				column = cColumn;
				
				tokenStream(new Token(c,tokenType,filename,line,column));
				token = tokenType = null;
				continue;
			}
			if(Arrays.asList(LangConstants.digits).indexOf(c) >= 0) {
				if(!isNumber && token != null) {
					tokenStream(new Token(token,tokenType,filename,line,column));
					token = tokenType = null;
				}
				isNumber = true;
				tokenType = TokenType.NUMBER;
				token = (token != null) ? token + c : c;
				
				line = cLine;
				column = cColumn;
				
				continue;
			} else if(isNumber && Arrays.asList(LangConstants.numberPunctuation).indexOf(c) >= 0) {
				token += c;
				continue;
			} else if(isNumber) {
				if(token != null) tokenStream(new Token(token,tokenType,filename,line,column));
				token = tokenType = null;
				isNumber = false;
			}
			//CONTINUE CHECKING
			
			if(Arrays.asList(LangConstants.whitespace).indexOf(c) >= 0) {
				if(token != null) {
					tokenStream(new Token(token,filename,line,column));
				}
				token = tokenType = null;
			} else if(token == null) {
				
				line = cLine;
				column = cColumn;
				
				token = c;
			} else if(alphanumeric == (Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0)) {
				token += c;
			} else if(alphanumeric != (Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0)) {
				tokenStream(new Token(token,filename,line,column));
				token = c;
				
				line = cLine;
				column = cColumn;
				
			}
			alphanumeric = Arrays.asList(LangConstants.alphanumeric).indexOf(c) >= 0;
		}
	}
	public static void tokenStream(Token token) {
		if(token == null) return;
		if(token.isSignificant()) System.out.println(token);
	}
}
