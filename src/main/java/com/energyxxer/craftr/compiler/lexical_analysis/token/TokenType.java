package com.energyxxer.craftr.compiler.lexical_analysis.token;

/**
 * Utility class containing all token types, and a method to match a given
 * string to a token type, based on patterns present in LangConstants.
 */
public class TokenType {
	public static final String FILE_HEADER = "FILE_HEADER"; //Contains information about file type in attributes

	public static final String NULL = "NULL"; // Literal "null"
	public static final String MODIFIER = "MODIFIER"; // public, static,
														// synchronized...
	public static final String UNIT_TYPE = "UNIT_TYPE"; // entity, item
	public static final String UNIT_ACTION = "UNIT_ACTION"; // extends, base...
	public static final String BRACE = "BRACE"; // (, ), {, }...
	public static final String DATA_TYPE = "DATA_TYPE"; // int, float, string,
														// Region...
	public static final String OPERATOR = "OPERATOR"; // *,+,++,>,<...
	public static final String IDENTIFIER_OPERATOR = "IDENTIFIER_OPERATOR"; // ++,--...
	public static final String LOGICAL_NEGATION_OPERATOR = "LOGICAL_NEGATION_OPERATOR"; // !...
	public static final String KEYWORD = "KEYWORD"; // if, else...
	public static final String ACTION_KEYWORD = "ACTION_KEYWORD"; // continue, break...
	public static final String COMMENT = "COMMENT"; // //stuff
	public static final String DOT = "DOT"; // this[.]field...
	public static final String COMMA = "COMMA"; // {1[,] 2[,]...}
	public static final String COLON = "COLON"; // case 8[:]
	public static final String LAMBDA_ARROW = "LAMBDA_ARROW"; // ->
	public static final String NUMBER = "NUMBER"; // 0.1f
	public static final String STRING_LITERAL = "STRING_LITERAL"; // "STRING LITERAL"

	public static final String ANNOTATION_MARKER = "ANNOTATION_MARKER"; //@promise("fixed")
	public static final String BLOCKSTATE_MARKER = "BLOCKSTATE_MARKER"; //#
	public static final String BLOCKSTATE = "BLOCKSTATE"; // #variant=andesite
	
	public static final String BOOLEAN = "BOOLEAN"; // true, false
	public static final String IDENTIFIER = "IDENTIFIER"; // Anything else
	public static final String END_OF_STATEMENT = "END_OF_STATEMENT"; // ;
	public static final String END_OF_FILE = "END_OF_FILE"; // End of file

	public static boolean isSignificant(String tokenType) {
		return tokenType != COMMENT;
	}
}
