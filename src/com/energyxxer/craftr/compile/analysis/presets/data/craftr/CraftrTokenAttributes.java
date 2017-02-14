package com.energyxxer.craftr.compile.analysis.presets.data.craftr;

/**
 * Defines metadata for Craftr tokens.
 * */
public class CraftrTokenAttributes {
    /**
     * Defines a fake-keyword that appears to be special,
     * but that is actually just an identifier. E.g: this
     * */
    public static final String IS_PSEUDO_KEYWORD = "IS_PSEUDO_KEYWORD";

    /**
     * Specifies that the given token is an enumeration.
     * E.g: Block, Particle, Enchantment...
     * */
    public static final String IS_ENUM = "IS_ENUM";
    /**
     * Specifies that the given token represents an entity.
     * E.g: armor_stand, horse, living_base...
     * */
    public static final String IS_ENTITY = "IS_ENTITY";
    /**
     * Specifies that the given token represents an item. (CURRENTLY UNUSED)
     * E.g: carrot_on_a_stick, stone, iron_sword...
     * */
    public static final String IS_ITEM = "IS_ITEM";

    /**
     * Specifies that the given BRACE-type token represents parentheses ().
     * */
    public static final String PARENTHESES = "PARENTHESES";
    /**
     * Specifies that the given BRACE-type token represents curly braces {}.
     * */
    public static final String CURLY_BRACES = "CURLY_BRACES";
    /**
     * Specifies that the given BRACE-type token represents square braces [].
     * */
    public static final String SQUARE_BRACES = "SQUARE_BRACES";

    /**
     * Specifies that the given BRACE-type token represents an opening brace.
     * E.g: (, {, [
     * */
    public static final String OPENING_BRACE = "OPENING_BRACE";
    /**
     * Specifies that the given BRACE-type token represents a closing brace.
     * E.g: ), }, ]
     * */
    public static final String CLOSING_BRACE = "CLOSING_BRACE";

    /**
     * Specifies that the given token is part of an annotation.
     * */
    public static final String IS_ANNOTATION = "IS_ANNOTATION";
    /**
     * Specifies that the given token is part of an annotation header.
     * */
    public static final String IS_ANNOTATION_HEADER = "IS_ANNOTATION_HEADER";
}
