package com.energyxxer.cbe.analysis.token;

import java.util.Arrays;

import com.energyxxer.cbe.analysis.LangConstants;

/**
 * Utility class containing all token types, and a
 * method to match a given string to a token type,
 * based on patterns present in LangConstants.
 * */
public class TokenType {
	public static final String NULL = "NULL"; //Literal "null"
	public static final String LINE_BREAK = "LINE_BREAK"; // \n
	public static final String QUALIFIER = "QUALIFIER"; //public, static, synchronized...
	public static final String UNIT_TYPE = "UNIT_TYPE"; //entity, item
	public static final String UNIT_ACTION = "UNIT_ACTION"; //extends, base...
	public static final String BRACE = "BRACE"; //(, ), {, }...
	public static final String DATA_TYPE = "DATA_TYPE"; //int, float, string, Region...
	public static final String OPERATOR = "OPERATOR"; //*,+,++,>,<...
	public static final String STATEMENT = "STATEMENT"; // if, else...
	public static final String COMMENT = "COMMENT"; // //stuff
	public static final String DOT = "DOT"; //this[.]field...
	public static final String COMMA = "COMMA"; //{1[,] 2[,]...}
	public static final String NUMBER = "NUMBER"; // 0.1
	public static final String STRING_LITERAL = "STRING_LITERAL"; // "STRING LITERAL"
	public static final String BOOLEAN = "BOOLEAN"; //true, false
	public static final String IDENTIFIER = "IDENTIFIER"; //Anything else
	public static final String END_OF_STATEMENT = "END_OF_STATEMENT"; //;
	public static final String END_OF_FILE = "END_OF_FILE"; //End of file
	
	public static String getTypeOf(String token) {
		String[][] patterns = {LangConstants.qualifiers,LangConstants.unit_types,LangConstants.unit_actions,LangConstants.braces,LangConstants.data_types,LangConstants.statements,LangConstants.dots,LangConstants.commas,LangConstants.booleans};
		String[] types = {QUALIFIER,UNIT_TYPE,UNIT_ACTION,BRACE,DATA_TYPE,STATEMENT,DOT,COMMA,BOOLEAN};
		
		for(int i = 0; i < patterns.length; i++) {
			for(int j = 0; j < patterns[i].length; j++) {
				if(Arrays.asList(patterns[i]).contains(token)) {
					return types[i];
				}
			}
		}
		
		return IDENTIFIER;
	}
	
	public static boolean isSignificant(String tokenType) {
		return tokenType != COMMENT;
	}
}
