package com.energyxxer.cbe.analysis.token;

import static com.energyxxer.cbe.analysis.token.TokenType.*;
import static com.energyxxer.cbe.analysis.token.TokenAttributes.*;
import static com.energyxxer.util.StringUtil.FALSE;
import static com.energyxxer.util.StringUtil.TRUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.energyxxer.cbe.analysis.LangConstants;

public class TokenStream {
	
	public ArrayList<Token> tokens = new ArrayList<Token>();
	public HashMap<String, String> bufferData = new HashMap<String, String>();
	
	public final void write(Token token) {addEnvironmentAttributes(token); onWrite(token); tokens.add(token);}
	
	public TokenStream() {
		bufferData.put("IS_ANNOTATION", FALSE);
		bufferData.put("ANNOTATION_PHASE", "NONE");
	}
	
	public void addEnvironmentAttributes(Token token) {
		if(token.type == ANNOTATION) {
			bufferData.put("IS_ANNOTATION", TRUE);
			bufferData.put("ANNOTATION_PHASE", "ANNOTATION");
		}
		if(token.type == IDENTIFIER) {
			if(tokens.size() >= 2) {
				if(tokens.get(tokens.size()-1).type == DOT && Boolean.valueOf(true).equals(tokens.get(tokens.size()-2).attributes.get("IS_ENUM"))) {
					String enumName = tokens.get(tokens.size()-2).value;
					String enumValue = token.value;

					List<String> enums = LangConstants.enums;
					List<List<String>> enumGroups = LangConstants.enumValues;
					
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
	}
	
	public void onWrite(Token token) {}
}
