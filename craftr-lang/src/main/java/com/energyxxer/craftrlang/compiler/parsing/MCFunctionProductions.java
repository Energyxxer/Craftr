package com.energyxxer.craftrlang.compiler.parsing;

import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.mcfunction.MCFunction;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenGroupMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenItemMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenListMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenStructureMatch;

public class MCFunctionProductions {

    public static final TokenStructureMatch FILE = new TokenStructureMatch("FILE");

    public static final TokenStructureMatch LINE = new TokenStructureMatch("LINE");
    public static final TokenStructureMatch COMMAND = new TokenStructureMatch("COMMAND");

    public static final TokenStructureMatch INTEGER = new TokenStructureMatch("INTEGER");
    public static final TokenStructureMatch REAL_NUMBER = new TokenStructureMatch("REAL_NUMBER");
    public static final TokenStructureMatch TYPED_NUMBER = new TokenStructureMatch("TYPED_NUMBER");

    public static final TokenStructureMatch ANY_STRING = new TokenStructureMatch("ANY_STRING");

    public static final TokenStructureMatch SELECTOR = new TokenStructureMatch("SELECTOR");
    public static final TokenStructureMatch SELECTOR_ARGUMENT = new TokenStructureMatch("SELECTOR_ARGUMENT");

    public static final TokenStructureMatch INTEGER_NUMBER_RANGE = new TokenStructureMatch("INTEGER_NUMBER_RANGE");
    public static final TokenStructureMatch REAL_NUMBER_RANGE = new TokenStructureMatch("REAL_NUMBER_RANGE");

    static {
        FILE.add(new TokenListMatch(new TokenGroupMatch(true).append(LINE), new TokenItemMatch(MCFunction.NEWLINE)));

        {
            LINE.add(COMMAND);
            LINE.add(new TokenItemMatch(MCFunction.COMMENT));
        }

        {
            INTEGER.add(new TokenItemMatch(MCFunction.INTEGER_NUMBER));

            REAL_NUMBER.add(INTEGER);
            REAL_NUMBER.add(new TokenItemMatch(MCFunction.REAL_NUMBER));

            TYPED_NUMBER.add(REAL_NUMBER);
            TYPED_NUMBER.add(new TokenItemMatch(MCFunction.TYPED_NUMBER));

            ANY_STRING.add(new TokenItemMatch(TokenType.UNKNOWN));
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(MCFunction.SELECTOR_HEADER));

            {
                TokenGroupMatch g2 = new TokenGroupMatch(true);
                g2.append(new TokenItemMatch(MCFunction.BRACE, "["));
                g2.append(new TokenListMatch(SELECTOR_ARGUMENT, true));
                g2.append(new TokenItemMatch(MCFunction.BRACE, "]"));
            }
        }

        {
            INTEGER_NUMBER_RANGE.add(new TokenItemMatch(MCFunction.INTEGER_NUMBER));
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(INTEGER);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenGroupMatch(true).append(INTEGER));
                INTEGER_NUMBER_RANGE.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(INTEGER);
                INTEGER_NUMBER_RANGE.add(g);
            }

            REAL_NUMBER_RANGE.add(new TokenItemMatch(MCFunction.REAL_NUMBER));
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(REAL_NUMBER);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenGroupMatch(true).append(REAL_NUMBER));
                REAL_NUMBER_RANGE.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(REAL_NUMBER);
                REAL_NUMBER_RANGE.add(g);
            }
        }

        {
            //Integer Range Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("INTEGER_RANGE_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "level"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(INTEGER_NUMBER_RANGE);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Real Number Range Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("REAL_RANGE_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "distance"));
            s.add(new TokenItemMatch(null, "x_rotation"));
            s.add(new TokenItemMatch(null, "y_rotation"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(REAL_NUMBER_RANGE);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Integer Number Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("INTEGER_NUMBER_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "limit"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(INTEGER);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Real Number Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("REAL_NUMBER_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "x"));
            s.add(new TokenItemMatch(null, "y"));
            s.add(new TokenItemMatch(null, "z"));
            s.add(new TokenItemMatch(null, "dx"));
            s.add(new TokenItemMatch(null, "dy"));
            s.add(new TokenItemMatch(null, "dz"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(REAL_NUMBER);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //String Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("STRING_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "type"));
            s.add(new TokenItemMatch(null, "name"));
            s.add(new TokenItemMatch(null, "tag"));
            s.add(new TokenItemMatch(null, "team"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(new TokenItemMatch(null, "!", true));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE", true);
            s2.add(new TokenItemMatch(MCFunction.STRING_LITERAL));
            s2.add(ANY_STRING);

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Gamemode argument
            TokenGroupMatch g = new TokenGroupMatch().setName("GAMEMODE_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "gamemode"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE");
            s2.add(new TokenItemMatch(null, "survival"));
            s2.add(new TokenItemMatch(null, "creative"));
            s2.add(new TokenItemMatch(null, "adventure"));
            s2.add(new TokenItemMatch(null, "spectator"));

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Sort argument
            TokenGroupMatch g = new TokenGroupMatch().setName("SORT_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "sort"));

            g.append(s);
            g.append(new TokenItemMatch(MCFunction.EQUALS));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE");
            s2.add(new TokenItemMatch(null, "nearest"));
            s2.add(new TokenItemMatch(null, "furthest"));
            s2.add(new TokenItemMatch(null, "arbitrary"));
            s2.add(new TokenItemMatch(null, "random"));

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            TokenGroupMatch say = new TokenGroupMatch();
            say.append(new TokenGroupMatch().append(new TokenItemMatch(null, "say")).setName("COMMAND_HEADER"));
            say.append(SELECTOR);

            COMMAND.add(say);
        }
    }
}
