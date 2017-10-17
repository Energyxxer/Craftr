package com.energyxxer.craftrlang;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2/11/2017.
 */
public class CraftrLang {

    public static final TokenType
            NULL = new TokenType("NULL"), // Literal "null"
            MODIFIER = new TokenType("MODIFIER"), // public, static,
            UNIT_TYPE = new TokenType("UNIT_TYPE"), // entity, item
            UNIT_ACTION = new TokenType("UNIT_ACTION"), // extends, base...
            BRACE = new TokenType("BRACE"), // (, ), {, }...
            DATA_TYPE = new TokenType("DATA_TYPE"), // int, float, string,
            OPERATOR = new TokenType("OPERATOR"), // *,+,++,>,<...
            IDENTIFIER_OPERATOR = new TokenType("IDENTIFIER_OPERATOR"), // ++,--...
            LOGICAL_NEGATION_OPERATOR = new TokenType("LOGICAL_NEGATION_OPERATOR"), // !...
            KEYWORD = new TokenType("KEYWORD"), // if, else...
            ACTION_KEYWORD = new TokenType("ACTION_KEYWORD"), // continue, break...
            COMMENT = new TokenType("COMMENT", false), // //stuff
            DOT = new TokenType("DOT"), // this[.]field...
            COMMA = new TokenType("COMMA"), // {1[,] 2[,]...}
            COLON = new TokenType("COLON"), // case 8[:]
            LAMBDA_ARROW = new TokenType("LAMBDA_ARROW"), // ->
            NUMBER = new TokenType("NUMBER"), // 0.1f
            STRING_LITERAL = new TokenType("STRING_LITERAL"), // "STRING LITERAL"
            ANNOTATION_MARKER = new TokenType("ANNOTATION_MARKER"), // @promise("fixed")
            BLOCKSTATE_MARKER = new TokenType("BLOCKSTATE_MARKER"), // #
            BLOCKSTATE = new TokenType("BLOCKSTATE"), // #variant=andesite
            BOOLEAN = new TokenType("BOOLEAN"), // true, false
            IDENTIFIER = new TokenType("IDENTIFIER"), // Anything else
            END_OF_STATEMENT = new TokenType("END_OF_STATEMENT"); // ;

    public static final List<String>
        modifiers = Arrays.asList("public", "static", "abstract", "final", "protected", "private", "compilation", "ingame", "native"),
        unit_types = Arrays.asList("entity", "item", "feature", "class", "enum", "world"),
        unit_actions = Arrays.asList("extends", "implements", "requires"),
        data_types = Arrays.asList("int", "float", "double", "long", "boolean", "void", "char"),
        keywords = Arrays.asList("if", "else", "while", "for", "switch", "case", "default", "new", "event", "setup", "package", "import", "operator", "overrides"),
        action_keywords = Arrays.asList("break", "continue", "return"),
        booleans = Arrays.asList("true", "false"),
        nulls = Collections.singletonList("null"),
        entities = Arrays.asList("player", "bat", "chicken", "cow", "mooshroom", "pig", "rabbit", "sheep", "squid", "villager", "enderman", "polar_bear", "spider", "cave_spider", "zombie_pigman", "blaze", "creeper", "endermite", "ghast", "guardian", "elder_guardian", "magma_cube", "shulker", "silverfish", "skeleton", "stray", "wither_skeleton", "slime", "witch", "zombie", "husk", "zombie_villager", "horse", "donkey", "mule", "zombie_horse", "skeleton_horse", "ocelot", "wolf", "villager_golem", "snowman", "ender_dragon", "wither", "giant", "falling_block", "tnt", "boat", "minecart", "chest_minecart", "commandblock_minecart", "furnace_minecart", "hopper_minecart", "tnt_minecart", "spawner_minecart", "small_fireball", "dragon_fireball", "fireball", "spectral_arrow", "arrow", "thrown_xp_bottle", "thrown_egg", "thrown_ender_pearl", "eye_of_ender_signal", "thrown_snowball", "shulker_bullet", "thrown_potion", "wither_skull", "armor_stand", "ender_crystal", "item_frame", "leash_knot", "painting", "xp_orb", "item_entity", "lightning_bolt", "fireworks_rocket", "area_effect_cloud", "evocation_illager", "vex", "vindication_illager", "llama", "llama_spit", "evocation_fangs"),
        abstract_entities = Arrays.asList("entity_base", "living_base", "projectile_base", "minecart_base", "item_base");

    /**
     * Contains all pseudo-keywords.
     * */
    public static final List<String> pseudo_keywords = Arrays.asList("this", "super", "instanceof");

    public enum Modifier {
        PUBLIC, STATIC, ABSTRACT, FINAL, PROTECTED, PRIVATE, COMPILATION, INGAME, NATIVE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }
    public static TokenType classify(String token) {
        for(String p : modifiers) {
            if(token.equals(p)) {
                return MODIFIER;
            }
        }
        for(String p : unit_types) {
            if(token.equals(p)) {
                return UNIT_TYPE;
            }
        }
        for(String p : unit_actions) {
            if(token.equals(p)) {
                return UNIT_ACTION;
            }
        }
        for(String p : data_types) {
            if(token.equals(p)) {
                return DATA_TYPE;
            }
        }
        for(String p : keywords) {
            if(token.equals(p)) {
                return KEYWORD;
            }
        }
        for(String p : action_keywords) {
            if(token.equals(p)) {
                return ACTION_KEYWORD;
            }
        }
        for(String p : booleans) {
            if(token.equals(p)) {
                return BOOLEAN;
            }
        }
        for(String p : nulls) {
            if(token.equals(p)) {
                return NULL;
            }
        }

        return IDENTIFIER;
    }

    public static boolean isPseudoIdentifier(String name) {
        return name.equals("this") || name.equals("super");
    }

    public static boolean isValidIdentifier(String str) {
        if(str.length() <= 0) return false;
        for(int i = 0; i < str.length(); i++) {
            if((i == 0 && !Character.isJavaIdentifierStart(str.charAt(0))) || (!Character.isJavaIdentifierPart(str.charAt(i)))) {
                return false;
            }
        }
        return classify(str) == IDENTIFIER && !isPseudoIdentifier(str);
    }

    public static boolean isValidIdentifierPath(String str) {
        String[] segments = str.split("\\.",-1);
        if(segments.length <= 0) return false;
        for(String segment : segments) {
            if(!isValidIdentifier(segment)) return false;
        }
        return true;
    }
}
