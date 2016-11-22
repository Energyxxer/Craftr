package com.energyxxer.cbe.compile.analysis.token;

import static com.energyxxer.cbe.compile.analysis.token.TokenType.*;

import com.energyxxer.cbe.compile.analysis.LangConstants;

public class TokenAttributes {
	public static final String IS_ENUM = "IS_ENUM";
	public static final String IS_ENTITY = "IS_ENTITY";
	public static final String IS_ITEM = "IS_ITEM";

	public static final String PARENTHESES = "PARENTHESES";
	public static final String CURLY_BRACES = "CURLY_BRACES";
	public static final String SQUARE_BRACES = "SQUARE_BRACES";

	public static final String OPENING_BRACE = "OPENING_BRACE";
	public static final String CLOSING_BRACE = "CLOSING_BRACE";
	
	public static void giveAttributes(Token t) {
		if(t.type == IDENTIFIER) {
			t.attributes.put(IS_ENUM, LangConstants.enums.contains(t.value));
			t.attributes.put(IS_ENTITY, LangConstants.entities.contains(t.value));
		}
		if(t.type == BRACE) {
			if("()".contains(t.value)) t.attributes.put("BRACE_STYLE", PARENTHESES);
			if("{}".contains(t.value)) t.attributes.put("BRACE_STYLE", CURLY_BRACES);
			if("[]".contains(t.value)) t.attributes.put("BRACE_STYLE", SQUARE_BRACES);

			if("({[".contains(t.value)) t.attributes.put("BRACE_TYPE", OPENING_BRACE);
			if(")}]".contains(t.value)) t.attributes.put("BRACE_TYPE", CLOSING_BRACE);
		}
	}
}
