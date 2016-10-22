package com.energyxxer.syntax;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.energyxxer.util.MinecraftConstants;

/**
 * Defines what and how character sequences should be highlighted in a text
 * editor.
 */
public class CBESyntax extends Syntax {
	public static HashMap<String, ArrayList<String>> patterns = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, HashMap<String, Object>> styles = new HashMap<String, HashMap<String, Object>>();

	public static final CBESyntax INSTANCE = new CBESyntax();

	static {
		ArrayList<String> comment = new ArrayList<String>();
		comment.add("//");
		patterns.put("comment", comment);

		ArrayList<String> multilinecomment = new ArrayList<String>();
		multilinecomment.add("/*");
		multilinecomment.add("*/");
		patterns.put("multilinecomment", multilinecomment);

		ArrayList<String> entity = new ArrayList<String>();
		entity.addAll(MinecraftConstants.entities);
		entity.add("unknown");
		patterns.put("entity", entity);

		ArrayList<String> enums = new ArrayList<String>();
		enums.add("Block");
		enums.add("Item");
		enums.add("Gamemode");
		enums.add("Stat");
		enums.add("Achievement");
		enums.add("StatusEffect");
		enums.add("Particle");
		enums.add("Enchantment");
		patterns.put("enum", enums);

		ArrayList<String> digit = new ArrayList<String>();
		digit.add("0");
		digit.add("1");
		digit.add("2");
		digit.add("3");
		digit.add("4");
		digit.add("5");
		digit.add("6");
		digit.add("7");
		digit.add("8");
		digit.add("9");
		patterns.put("number", digit);

		ArrayList<String> keyword = new ArrayList<String>();
		keyword.add("entity");
		keyword.add("extends");
		keyword.add("this");
		keyword.add("if");
		keyword.add("else");
		keyword.add("base");
		keyword.add("string");
		keyword.add("int");
		keyword.add("float");
		keyword.add("boolean");
		keyword.add("public");
		keyword.add("static");
		keyword.add("synchronized");
		patterns.put("keyword", keyword);

		ArrayList<String> function = new ArrayList<String>();
		function.add("onTick");
		function.add("onInit");
		function.add("onHurt");
		function.add("onDeath");
		patterns.put("function", function);

		ArrayList<String> booleans = new ArrayList<String>();
		booleans.add("true");
		booleans.add("false");
		patterns.put("boolean", booleans);

		ArrayList<String> punctuation = new ArrayList<String>();
		punctuation.add(".");
		punctuation.add(";");
		patterns.put("punctuation", punctuation);

		ArrayList<String> brace = new ArrayList<String>();
		brace.add("(");
		brace.add(")");
		brace.add("[");
		brace.add("]");
		brace.add("{");
		brace.add("}");
		patterns.put("brace", brace);

		ArrayList<String> string = new ArrayList<String>();
		string.add("\"");
		patterns.put("string", string);

		ArrayList<String> operator = new ArrayList<String>();
		operator.add("+");
		operator.add("-");
		operator.add("/");
		operator.add("%");
		operator.add("*");
		operator.add("=");
		operator.add(":");
		operator.add("<");
		operator.add(">");
		operator.add("!");
		patterns.put("operator", operator);
	}

	static {
		HashMap<String, Object> comment = new HashMap<String, Object>();
		comment.put("color", Color.decode("#777777"));
		comment.put("italic", new Boolean(true));
		styles.put("comment", comment);

		HashMap<String, Object> multilinecomment = new HashMap<String, Object>();
		multilinecomment.put("color", Color.decode("#777777"));
		multilinecomment.put("italic", new Boolean(true));
		styles.put("multilinecomment", multilinecomment);

		HashMap<String, Object> entity = new HashMap<String, Object>();
		entity.put("color", Color.decode("#447722"));
		entity.put("whole", new Boolean(true));
		styles.put("entity", entity);

		HashMap<String, Object> enums = new HashMap<String, Object>();
		enums.put("color", Color.decode("#BB44AA"));
		enums.put("whole", new Boolean(true));
		//styles.put("enum", enums);

		HashMap<String, Object> digit = new HashMap<String, Object>();
		digit.put("color", Color.decode("#E89039"));
		styles.put("number", digit);

		HashMap<String, Object> keyword = new HashMap<String, Object>();
		keyword.put("color", Color.decode("#6655CC"));
		keyword.put("whole", new Boolean(true));
		keyword.put("bold", new Boolean(true));
		styles.put("keyword", keyword);

		HashMap<String, Object> function = new HashMap<String, Object>();
		function.put("color", Color.decode("#268677"));
		function.put("whole", new Boolean(true));
		function.put("bold", new Boolean(true));
		styles.put("function", function);

		HashMap<String, Object> booleans = new HashMap<String, Object>();
		booleans.put("color", Color.decode("#6655CC"));
		booleans.put("bold", new Boolean(true));
		booleans.put("italic", new Boolean(true));
		booleans.put("whole", new Boolean(true));
		styles.put("boolean", booleans);

		HashMap<String, Object> punctuation = new HashMap<String, Object>();
		punctuation.put("color", Color.decode("#324f32"));
		styles.put("punctuation", punctuation);

		HashMap<String, Object> brace = new HashMap<String, Object>();
		brace.put("color", Color.decode("#000000"));

		HashMap<String, Object> string = new HashMap<String, Object>();
		string.put("color", Color.decode("#668844"));
		styles.put("string_literal", string);

		HashMap<String, Object> operator = new HashMap<String, Object>();
		operator.put("color", Color.decode("#a72d2d"));
		styles.put("operator", operator);

		HashMap<String, Object> specials = new HashMap<String, Object>();
		//specials.put("color", Color.decode("#553800"));
		specials.put("bold", new Boolean(true));

		HashMap<String, Object> nulls = new HashMap<String, Object>();
		nulls.put("color", Color.decode("#880000"));
		nulls.put("bold", new Boolean(true));

		HashMap<String, Object> enum_values = new HashMap<String, Object>();
		enum_values.put("color", Color.decode("#000088"));
		enum_values.put("bold", new Boolean(true));
		
		HashMap<String, Object> annotation_header = new HashMap<String, Object>();
		annotation_header.put("color", Color.decode("#6A6A6A"));

		HashMap<String, Object> annotation = new HashMap<String, Object>();
		//annotation.put("color", Color.decode("#6A6A6A"));
		annotation.put("italic", new Boolean(true));
		
		//TEST
		{
			styles.put("qualifier", keyword);
			styles.put("unit_type", keyword);
			styles.put("unit_action", keyword);
			styles.put("brace", brace);
			styles.put("data_type", keyword);
			styles.put("statement", keyword);
			styles.put("dot", punctuation);
			styles.put("comma", punctuation);
			styles.put("number", digit);
			styles.put("boolean", booleans);
			styles.put("annotation", annotation_header);
			styles.put("special", keyword);
			styles.put("null", nulls);
			styles.put("end_of_statement", punctuation);
			styles.put("#is_entity", entity);
			styles.put("#is_enum", enums);
			styles.put("#is_enum_value", enum_values);
			styles.put("#is_annotation_header", annotation_header);
			styles.put("#is_annotation", annotation);
		}
	}

	@Override
	public HashMap<String, ArrayList<String>> getPatterns() {
		return patterns;
	}

	@Override
	public HashMap<String, HashMap<String, Object>> getStyles() {
		return styles;
	}
}
