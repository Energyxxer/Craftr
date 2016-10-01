package com.energyxxer.cbe.parsing;

/**
 * Contains most of the language's token patterns.
 * */
public class LangConstants {
	public static final String[] whitespace = " \n\t".split("");
	public static final String[] alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".split("");
	public static final String[] digits = "0123456789".split("");
	
	public static final String[] stringLiteral = {"\""};
	public static final String[] comment = {"//"};
	public static final String[] multilinecomment = {"/*", "*/"};
	
	public static final String[] numberPunctuation = {"."};
	public static final String[] end_of_statement = {";"};
	
	public static final String[] qualifiers = {"public","static","synchronized"};
	public static final String[] unit_types = {"entity","item"};
	public static final String[] unit_actions = {"extends","when","base"};
	public static final String[] braces = {"(",")","[","]","{","}"};
	public static final String[] data_types = {"int","string","float","Point","Region"};
	public static final String[] operators = {"++","+","-","*","/","%","<=",">=","==","=","<",">","^"};
	public static final String[] statements = {"if","else","while","for"};
	public static final String[] dots = {"."};
	public static final String[] commas = {","};
	public static final String[] booleans = {"true","false"};
	public static final String[] nulls = {"null"};
}
