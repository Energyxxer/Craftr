package com.energyxxer.craftrlang.compiler.lexical_analysis.presets.mcfunction;

import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;

public class MCFunction {

    public static final TokenType
            SELECTOR_HEADER = new TokenType("SELECTOR_HEADER"), // @a, @e, @r, @p, @s
            BRACE = new TokenType("BRACE"), // (, ), {, }...
            DOT = new TokenType("DOT"), // .
            COMMA = new TokenType("COMMA"), // {1[,] 2[,]...}
            COLON = new TokenType("COLON"), // case 8[:]
            EQUALS = new TokenType("EQUALS"), // =
            TILDE = new TokenType("TILDE"), // ~
            CARET = new TokenType("CARET"), // ^
            INTEGER_NUMBER = new TokenType("INTEGER_NUMBER"), // 1
            REAL_NUMBER = new TokenType("REAL_NUMBER"), // 0.1
            TYPED_NUMBER = new TokenType("TYPED_NUMBER"), // 0.1f
            STRING_LITERAL = new TokenType("STRING_LITERAL"), // "STRING LITERAL"
            //BOOLEAN = new TokenType("BOOLEAN"), // true, false
            NEWLINE = new TokenType("NEWLINE"),
            COMMENT = new TokenType("COMMENT"),
            IDENTIFIER = new TokenType("IDENTIFIER"); // Anything else
}
