package com.energyxxer.cbe.compile.analysis;

import com.energyxxer.cbe.minecraft.MinecraftConstants;

import java.util.Arrays;
import java.util.List;

/**
 * Contains most of the language's token patterns.
 */
public class LangConstants {
	public static final String[] whitespace = " \n\t".split("");
	public static final String[] alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".split("");
	public static final String[] digits = "0123456789".split("");

	public static final String[] stringLiteral = { "\"" };
	public static final String[] comment = { "//" };
	public static final String[] multilinecomment = { "/*", "*/" };
	public static final String[] annotations = { "@" };

	public static final String[] numberPunctuation = { "." };
	public static final String[] end_of_statement = { ";" };

	public static final String[] modifiers = { "public", "static", "synchronized", "local" };
	public static final String[] unit_types = { "entity", "item", "feature", "class" };
	public static final String[] unit_actions = { "extends", "implements", "requires" };
	public static final String[] braces = { "(", ")", "[", "]", "{", "}" };
	public static final String[] data_types = { "int", "String", "float", "boolean", "Point", "Dimensions", "Region", "void" };
	public static final String[] operators = { "+=", "+", "-=", "-", "*=", "*", "/=", "/", "%=", "%", "<=", ">=", "==", "=", "<", ">", "^=", "^" };
	public static final String[] identifier_operators = { "++", "--" };
	public static final String[] logical_negation_operators = { "!" };
	public static final String[] keywords = { "if", "else", "while", "for", "switch", "case", "default", "new", "event", "package", "import" };
	public static final String[] action_keywords = { "break", "continue", "return" };
	public static final String[] dots = { "." };
	public static final String[] commas = { "," };
	public static final String[] colons = { ":" };
	public static final String[] booleans = { "true", "false" };
	public static final String[] nulls = { "null" };

	public static final List<String> enums = Arrays.asList("Block", "Item", "Gamemode", "Stat", "Achievement", "Effect", "Particle", "Enchantment", "Dimension");

	public static final List<String> pseudo_keywords = Arrays.asList("this", "that");

	public static final String[] blockstate_marker = { "#" };
	public static final List<String> blockstate_specials = Arrays.asList("default");
	
	public static final List<List<String>> enumValues = Arrays.asList(MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.gamemode_enums,MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.effect_enums,MinecraftConstants.particle_enums,MinecraftConstants.enchantment_enums,MinecraftConstants.dimension_enums);
	
	public static final List<String> entities = MinecraftConstants.entities;
}
