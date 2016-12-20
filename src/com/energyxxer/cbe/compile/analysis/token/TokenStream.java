package com.energyxxer.cbe.compile.analysis.token;

import com.energyxxer.cbe.compile.analysis.LangConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.energyxxer.cbe.compile.analysis.token.TokenAttributes.*;
import static com.energyxxer.cbe.compile.analysis.token.TokenType.*;
import static com.energyxxer.cbe.util.StringUtil.FALSE;
import static com.energyxxer.cbe.util.StringUtil.TRUE;

public class TokenStream implements Iterable<Token> {
	
	public ArrayList<Token> tokens = new ArrayList<Token>();
	public HashMap<String, String> bufferData = new HashMap<String, String>();
	public ArrayList<Token> tokenBuffer = new ArrayList<Token>();

	private boolean includeInsignificantTokens = false;

	public TokenStream() {includeInsignificantTokens = false;}
	public TokenStream(boolean includeInsignificantTokens) {
		this.includeInsignificantTokens = includeInsignificantTokens;
	}
	
	public final void write(Token token) {
		write(token, false);
	}
	
	public final void write(Token token, boolean skip) {
		if(skip || !addEnvironmentAttributes(token)) {
			onWrite(token);
			if(!token.isSignificant() && !includeInsignificantTokens) return;
			tokens.add(token);
		}
	}
	
	{
		bufferData.put("IS_ANNOTATION", FALSE);
		bufferData.put("ANNOTATION_PHASE", "NONE");

		bufferData.put("IS_BLOCKSTATE", FALSE);
		bufferData.put("BLOCKSTATE_PHASE", "NONE");
	}
	
	public boolean addEnvironmentAttributes(Token token) {
		
		boolean cancel = false;
		
		if(addAnnotationAttributes(token)) cancel = true;
		if(addBlockstateAttributes(token)) cancel = true;
		
		return cancel;
	}
	
	private boolean addBlockstateAttributes(Token token) {
		boolean cancel = false;
		if(token.type == BLOCKSTATE_MARKER && bufferData.get("BLOCKSTATE_PHASE").equals("NONE")) {
			bufferData.put("IS_BLOCKSTATE", TRUE);
			bufferData.put("BLOCKSTATE_PHASE", "KEY_FIRST");
			tokenBuffer.add(token);
			cancel = true;
		} else if((token.type == IDENTIFIER && bufferData.get("BLOCKSTATE_PHASE").startsWith("KEY")) || (LangConstants.blockstate_specials.contains(token.value) && bufferData.get("BLOCKSTATE_PHASE").equals("KEY_FIRST"))) {
			tokenBuffer.add(token);
			cancel = true;
			if(LangConstants.blockstate_specials.contains(token.value)) {
				//Is special (#default...)
				
				write(Token.merge(BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
				tokenBuffer.clear();

				bufferData.put("IS_BLOCKSTATE", FALSE);
				bufferData.put("BLOCKSTATE_PHASE", "NONE");
			} else {
				//Not special (#variant...)

				bufferData.put("BLOCKSTATE_PHASE", "EQUALS");
			}
		} else if(token.value.equals("=") && bufferData.get("BLOCKSTATE_PHASE").equals("EQUALS")) {
			tokenBuffer.add(token);
			bufferData.put("BLOCKSTATE_PHASE", "VALUE");
			cancel = true;
		} else if((token.type == IDENTIFIER || token.type == BOOLEAN) && bufferData.get("BLOCKSTATE_PHASE").equals("VALUE")) {
			tokenBuffer.add(token);
			bufferData.put("BLOCKSTATE_PHASE", "NEXT");
			cancel = true;
		} else if(bufferData.get("BLOCKSTATE_PHASE").equals("NEXT")) {
			if(token.type == COMMA) {
				tokenBuffer.add(token);
				bufferData.put("BLOCKSTATE_PHASE", "KEY");
				cancel = true;
			} else if(token.type == END_OF_STATEMENT) {
				tokenBuffer.add(token);
				write(Token.merge(BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
				tokenBuffer.clear();

				bufferData.put("IS_BLOCKSTATE", FALSE);
				bufferData.put("BLOCKSTATE_PHASE", "NONE");
				cancel = true;
			} else {
				write(Token.merge(BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
				tokenBuffer.clear();

				bufferData.put("IS_BLOCKSTATE", FALSE);
				bufferData.put("BLOCKSTATE_PHASE", "NONE");
			}
		} else if(bufferData.get("IS_BLOCKSTATE") == TRUE) {
			//Whoops something went wrong
			
			write(Token.merge(BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
			tokenBuffer.clear();

			bufferData.put("IS_BLOCKSTATE", FALSE);
			bufferData.put("BLOCKSTATE_PHASE", "NONE");
			
		}
		return cancel;
	}
	
	private boolean addAnnotationAttributes(Token token) {
		if(token.type == ANNOTATION_MARKER && bufferData.get("ANNOTATION_PHASE").equals("NONE")) {
			bufferData.put("IS_ANNOTATION", TRUE);
			bufferData.put("ANNOTATION_PHASE", "ANNOTATION");
		}
		if(token.type == IDENTIFIER) {
			if(tokens.size() >= 2) {
				if(tokens.get(tokens.size()-1).type == DOT && Boolean.valueOf(true).equals(tokens.get(tokens.size()-2).attributes.get("IS_ENUM"))) {
					String enumName = tokens.get(tokens.size()-2).value;
					String enumValue = token.value;

					List<String> enums = LangConstants.enums;
					List<List<String>> enumGroups = LangConstants.enum_values;
					
					if(enums.contains(enumName)) {
						if(enumGroups.get(enums.indexOf(enumName)).contains(enumValue)) {
							token.attributes.put("IS_ENUM_VALUE", new Boolean(true));
						}
					}
				}
			}
		}
		if(bufferData.get("IS_ANNOTATION") == TRUE) {
			token.attributes.put("IS_ANNOTATION", new Boolean(true));
			switch(bufferData.get("ANNOTATION_PHASE")) {
				case "ANNOTATION": {
					bufferData.put("ANNOTATION_PHASE", "IDENTIFIER");
					break;
				} case "IDENTIFIER": {
					if(token.type == IDENTIFIER) {
						token.attributes.put("IS_ANNOTATION_HEADER", new Boolean(true));
						bufferData.put("ANNOTATION_PHASE", "BRACE_OPEN");
					} else {
						bufferData.put("IS_ANNOTATION", FALSE);
						bufferData.put("ANNOTATION_PHASE", "NONE");
					}
					break;
				} case "BRACE_OPEN": {
					if(token.type == BRACE && token.attributes.get("BRACE_STYLE") == PARENTHESES && token.attributes.get("BRACE_TYPE") == OPENING_BRACE) {
						bufferData.put("ANNOTATION_PHASE", "BRACE_CLOSE");
					} else {
						bufferData.put("IS_ANNOTATION", FALSE);
						bufferData.put("ANNOTATION_PHASE", "NONE");
					}
					break;
				} case "BRACE_CLOSE": {
					if(token.type == BRACE && token.attributes.get("BRACE_STYLE") == PARENTHESES && token.attributes.get("BRACE_TYPE") == CLOSING_BRACE) {
						bufferData.put("IS_ANNOTATION", FALSE);
						bufferData.put("ANNOTATION_PHASE", "NONE");
					}
					break;
				} case "NONE": {
					break;
				} default: {
					bufferData.put("IS_ANNOTATION", FALSE);
					bufferData.put("ANNOTATION_PHASE", "NONE");
				}
			}
		}
		return false;
	}
	
	public void onWrite(Token token) {}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}
}
