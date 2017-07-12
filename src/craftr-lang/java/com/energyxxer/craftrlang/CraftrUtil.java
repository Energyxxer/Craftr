package com.energyxxer.craftrlang;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2/11/2017.
 */
public class CraftrUtil {

    private static final List<String>
        modifiers = Arrays.asList("public", "static", "abstract", "final", "protected", "private", "compilation", "ingame"),
        unit_types = Arrays.asList("entity", "item", "feature", "class", "enum"),
        unit_actions = Arrays.asList("extends", "implements", "requires"),
        data_types = Arrays.asList("int", "String", "float", "boolean", "void", "char"),
        keywords = Arrays.asList("if", "else", "while", "for", "switch", "case", "default", "new", "event", "init", "package", "import", "operator", "instanceof"),
        action_keywords = Arrays.asList("break", "continue", "return"),
        booleans = Arrays.asList("true", "false"),
        nulls = Collections.singletonList("null");

    /**
     * Contains all pseudo-keywords.
     * */
    public static final List<String> pseudo_keywords = Arrays.asList("this", "that", "Thread", "compare", "stack", "nbt", "equipment", "multipart");

    public enum Modifier {
        PUBLIC, STATIC, ABSTRACT, FINAL, PROTECTED, PRIVATE, COMPILATION, INGAME;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public static String classify(String token) {
        for(String p : modifiers) {
            if(token.equals(p)) {
                return TokenType.MODIFIER;
            }
        }
        for(String p : unit_types) {
            if(token.equals(p)) {
                return TokenType.UNIT_TYPE;
            }
        }
        for(String p : unit_actions) {
            if(token.equals(p)) {
                return TokenType.UNIT_ACTION;
            }
        }
        for(String p : data_types) {
            if(token.equals(p)) {
                return TokenType.DATA_TYPE;
            }
        }
        for(String p : keywords) {
            if(token.equals(p)) {
                return TokenType.KEYWORD;
            }
        }
        for(String p : action_keywords) {
            if(token.equals(p)) {
                return TokenType.ACTION_KEYWORD;
            }
        }
        for(String p : booleans) {
            if(token.equals(p)) {
                return TokenType.BOOLEAN;
            }
        }
        for(String p : nulls) {
            if(token.equals(p)) {
                return TokenType.NULL;
            }
        }

        return TokenType.IDENTIFIER;
    }

    public static boolean isValidIdentifier(String str) {
        if(str.length() <= 0) return false;
        for(int i = 0; i < str.length(); i++) {
            if((i == 0 && !Character.isJavaIdentifierStart(str.charAt(0))) || (!Character.isJavaIdentifierPart(str.charAt(i)))) {
                return false;
            }
        }
        return classify(str) == TokenType.IDENTIFIER;
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
