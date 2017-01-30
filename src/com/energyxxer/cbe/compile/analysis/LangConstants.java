package com.energyxxer.cbe.compile.analysis;

import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.minecraft.MinecraftConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains most of the language's keywords.
 */
public class LangConstants {
	public static final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";

	public static final String[] string_literal = { "\"", "'", "`" };
	public static final String[] multi_line_string_literal = { "`" };
	public static final String[] comment = { "//" };
	public static final String[] multi_line_comment = { "/*", "*/" };
	public static final String[] annotations = { "@" };

	public static final String[] number_punctuation = { "." };
	public static final String[] number_type_suffix = { "b", "d", "f", "s", "L" };
	public static final String[] end_of_statement = { ";" };

	public static final String[] modifiers = { "public", "static", "typestatic", "abstract", "final", "protected", "private", "synchronized", "compilation", "ingame" };
	public static final String[] unit_types = { "entity", "item", "feature", "class" };
	public static final String[] unit_actions = { "extends", "implements" };
	public static final String[] braces = { "(", ")", "[", "]", "{", "}" };
	public static final String[] data_types = { "int", "String", "float", "boolean", "type", "void", "Thread" };
	public static final String[] operators = { "+=", "+", "-=", "-", "*=", "*", "/=", "/", "%=", "%", "<=", ">=", "!=", "==", "=", "<", ">", "^=", "^" };
	public static final String[] identifier_operators = { "++", "--" };
	public static final String[] logical_negation_operators = { "!" };
	public static final String[] keywords = { "if", "else", "while", "for", "switch", "case", "default", "new", "event", "init", "package", "import", "operator"};
	public static final String[] action_keywords = { "break", "continue", "return" };
	public static final String[] dots = { "." };
	public static final String[] commas = { "," };
	public static final String[] colons = { ":" };
	public static final String[] lambda = { "->" };
	public static final String[] booleans = { "true", "false" };
	public static final String[] nulls = { "null" };

	public static final List<String> enums = Arrays.asList("Block", "Item", "Gamemode", "Stat", "Achievement", "Effect", "Particle", "Enchantment", "Dimension");

	public static final List<String> pseudo_keywords = Arrays.asList("this", "that");

	public static final String[] blockstate_marker = { "#" };
	public static final List<String> blockstate_specials = Collections.singletonList("default");
	public static final String[] blockstate_end = { "|" };
	
	public static final List<List<String>> enum_values = Arrays.asList(MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.gamemode_enums,MinecraftConstants.block_enums,MinecraftConstants.block_enums,MinecraftConstants.effect_enums,MinecraftConstants.particle_enums,MinecraftConstants.enchantment_enums,MinecraftConstants.dimension_enums);
	
	public static final List<String> entities = new ArrayList<>(MinecraftConstants.entities);
	public static final List<String> abstract_entities = Arrays.asList("entity_base", "living_base");

	public static final List<SpecialTokenConstant> specialConstants;

	static {
		entities.addAll(abstract_entities);

		ArrayList<SpecialTokenConstant> specials = new ArrayList<>();
		specials.add(new SpecialTokenConstant(lambda, TokenType.LAMBDA_ARROW));
		specials.add(new SpecialTokenConstant(operators, TokenType.OPERATOR));
		specials.add(new SpecialTokenConstant(identifier_operators, TokenType.IDENTIFIER_OPERATOR));
		specials.add(new SpecialTokenConstant(logical_negation_operators, TokenType.LOGICAL_NEGATION_OPERATOR));
		specials.add(new SpecialTokenConstant(braces, TokenType.BRACE));
		specials.add(new SpecialTokenConstant(annotations, TokenType.ANNOTATION_MARKER));
		specials.add(new SpecialTokenConstant(end_of_statement, TokenType.END_OF_STATEMENT));

		specialConstants = specials;
	}
}
