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

    public static final TokenStructureMatch INTEGER_NUMBER = new TokenStructureMatch("INTEGER_NUMBER");
    public static final TokenStructureMatch REAL_NUMBER = new TokenStructureMatch("REAL_NUMBER");
    public static final TokenStructureMatch TYPED_NUMBER = new TokenStructureMatch("TYPED_NUMBER");

    public static final TokenStructureMatch ANY_STRING = new TokenStructureMatch("ANY_STRING");
    public static final TokenStructureMatch POSSIBLE_STRING = new TokenStructureMatch("POSSIBLE_STRING");
    public static final TokenStructureMatch BOOLEAN = new TokenStructureMatch("BOOLEAN");
    public static final TokenStructureMatch NAMESPACE = new TokenStructureMatch("NAMESPACE");
    public static final TokenStructureMatch RESOURCE_LOCATION = new TokenStructureMatch("RESOURCE_LOCATION");
    public static final TokenStructureMatch TEXT_COMPONENT = new TokenStructureMatch("TEXT_COMPONENT");

    public static final TokenStructureMatch BLOCKSTATE = new TokenStructureMatch("BLOCKSTATE");

    public static final TokenStructureMatch BLOCK = new TokenStructureMatch("BLOCK");
    public static final TokenStructureMatch BLOCK_TAGGED = new TokenStructureMatch("BLOCK_TAGGED");
    public static final TokenStructureMatch ITEM = new TokenStructureMatch("ITEM");
    public static final TokenStructureMatch ITEM_TAGGED = new TokenStructureMatch("ITEM_TAGGED");

    public static final TokenStructureMatch NBT_COMPOUND = new TokenStructureMatch("NBT_COMPOUND");
    public static final TokenStructureMatch NBT_VALUE = new TokenStructureMatch("NBT_VALUE");

    public static final TokenStructureMatch ENTITY = new TokenStructureMatch("ENTITY");

    public static final TokenStructureMatch SINGLE_COORDINATE = new TokenStructureMatch("SINGLE_COORDINATE");
    public static final TokenStructureMatch ABSOLUTE_COORDINATE = new TokenStructureMatch("ABSOLUTE_COORDINATE");
    public static final TokenStructureMatch RELATIVE_COORDINATE = new TokenStructureMatch("RELATIVE_COORDINATE");
    public static final TokenStructureMatch LOCAL_COORDINATE = new TokenStructureMatch("LOCAL_COORDINATE");
    public static final TokenStructureMatch MIXABLE_COORDINATE = new TokenStructureMatch("MIXABLE_COORDINATE");
    public static final TokenStructureMatch COORDINATE_SET = new TokenStructureMatch("COORDINATE_SET");
    public static final TokenStructureMatch TWO_COORDINATE_SET = new TokenStructureMatch("TWO_COORDINATE_SET");

    public static final TokenStructureMatch SELECTOR = new TokenStructureMatch("SELECTOR");
    public static final TokenStructureMatch SELECTOR_ARGUMENT = new TokenStructureMatch("SELECTOR_ARGUMENT");

    public static final TokenStructureMatch INTEGER_NUMBER_RANGE = new TokenStructureMatch("INTEGER_NUMBER_RANGE");
    public static final TokenStructureMatch REAL_NUMBER_RANGE = new TokenStructureMatch("REAL_NUMBER_RANGE");

    public static final TokenStructureMatch GAMEMODE = new TokenStructureMatch("GAMEMODE");
    public static final TokenStructureMatch SORTING = new TokenStructureMatch("SORTING");

    static {
        FILE.add(new TokenGroupMatch().append(new TokenListMatch(LINE, new TokenItemMatch(MCFunction.NEWLINE))).append(new TokenItemMatch(TokenType.END_OF_FILE)));

        {
            LINE.add(COMMAND);
            LINE.add(new TokenItemMatch(MCFunction.COMMENT));
            LINE.add(new TokenGroupMatch());
        }

        {
            INTEGER_NUMBER.add(new TokenItemMatch(MCFunction.INTEGER_NUMBER));

            REAL_NUMBER.add(INTEGER_NUMBER);
            REAL_NUMBER.add(new TokenItemMatch(MCFunction.REAL_NUMBER));

            TYPED_NUMBER.add(REAL_NUMBER);
            TYPED_NUMBER.add(new TokenItemMatch(MCFunction.TYPED_NUMBER));

            ANY_STRING.add(new TokenItemMatch(TokenType.UNKNOWN));

            POSSIBLE_STRING.add(ANY_STRING);
            POSSIBLE_STRING.add(new TokenItemMatch(MCFunction.STRING_LITERAL));
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(TokenType.UNKNOWN));
            g.append(new TokenItemMatch(MCFunction.COLON));

            NAMESPACE.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenGroupMatch(true).append(NAMESPACE));
            g.append(new TokenListMatch(ANY_STRING, new TokenItemMatch(null, "/")));

            RESOURCE_LOCATION.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(MCFunction.BRACE, "["));
            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("BLOCKSTATE_PROPERTY");
                g2.append(new TokenItemMatch(TokenType.UNKNOWN).setName("BLOCKSTATE_PROPERTY_KEY"));
                g2.append(new TokenItemMatch(MCFunction.EQUALS));
                {
                    TokenStructureMatch s = new TokenStructureMatch("BLOCKSTATE_PROPERTY_VALUE");
                    s.add(REAL_NUMBER);
                    s.add(BOOLEAN);
                    s.add(new TokenItemMatch(TokenType.UNKNOWN));
                    g2.append(s);
                }
                g.append(new TokenListMatch(g2, new TokenItemMatch(MCFunction.COMMA), true));
            }
            g.append(new TokenItemMatch(MCFunction.BRACE, "]"));

            BLOCKSTATE.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(RESOURCE_LOCATION);
            g.append(new TokenGroupMatch(true).append(BLOCKSTATE));
            g.append(new TokenGroupMatch(true).append(NBT_COMPOUND));
            BLOCK.add(g);
            BLOCK_TAGGED.add(new TokenGroupMatch().append(new TokenItemMatch(null, "#", true).setName("TAG_HEADER")).append(BLOCK));
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(RESOURCE_LOCATION);
            g.append(new TokenGroupMatch(true).append(NBT_COMPOUND));
            ITEM.add(g);
            ITEM_TAGGED.add(new TokenGroupMatch().append(new TokenItemMatch(null, "#", true).setName("TAG_HEADER")).append(ITEM));
        }

        {

            TokenStructureMatch LOCAL_COORDINATE = new TokenStructureMatch("LOCAL_COORDINATE");
            LOCAL_COORDINATE.add(new TokenGroupMatch().append(new TokenItemMatch(MCFunction.CARET).setName("CARET")).append(new TokenGroupMatch(true).append(REAL_NUMBER)));

            TokenStructureMatch ABSOLUTE_COORDINATE = new TokenStructureMatch("ABSOLUTE_COORDINATE");
            ABSOLUTE_COORDINATE.add(REAL_NUMBER);

            TokenStructureMatch RELATIVE_COORDINATE = new TokenStructureMatch("RELATIVE_COORDINATE");
            RELATIVE_COORDINATE.add(new TokenGroupMatch().append(new TokenItemMatch(MCFunction.TILDE).setName("TILDE")).append(new TokenGroupMatch(true).append(REAL_NUMBER)));

            TokenStructureMatch MIXABLE_COORDINATE = new TokenStructureMatch("MIXABLE_COORDINATE");
            MIXABLE_COORDINATE.add(ABSOLUTE_COORDINATE);
            MIXABLE_COORDINATE.add(RELATIVE_COORDINATE);

            SINGLE_COORDINATE.add(MIXABLE_COORDINATE);
            SINGLE_COORDINATE.add(LOCAL_COORDINATE);

            {
                TokenGroupMatch g = new TokenGroupMatch().setName("MIXED_COORDINATE_SET");
                g.append(MIXABLE_COORDINATE);
                g.append(MIXABLE_COORDINATE);
                g.append(MIXABLE_COORDINATE);
                COORDINATE_SET.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch().setName("LOCAL_COORDINATE_SET");
                g.append(LOCAL_COORDINATE);
                g.append(LOCAL_COORDINATE);
                g.append(LOCAL_COORDINATE);
                COORDINATE_SET.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch().setName("MIXED_TWO_COORDINATE_SET");
                g.append(MIXABLE_COORDINATE);
                g.append(MIXABLE_COORDINATE);
                TWO_COORDINATE_SET.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch().setName("LOCAL_TWO_COORDINATE_SET");
                g.append(LOCAL_COORDINATE);
                g.append(LOCAL_COORDINATE);
                TWO_COORDINATE_SET.add(g);
            }
        }

        {
            TokenStructureMatch JSON_ELEMENT = new TokenStructureMatch("JSON_ELEMENT");

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.BRACE,"{"));
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenItemMatch(MCFunction.STRING_LITERAL));
                    g2.append(new TokenItemMatch(MCFunction.COLON));
                    g2.append(JSON_ELEMENT);
                    g.append(new TokenListMatch(g2, new TokenItemMatch(MCFunction.COMMA), true));
                }
                g.append(new TokenItemMatch(MCFunction.BRACE,"}"));
                JSON_ELEMENT.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.BRACE,"["));
                g.append(new TokenListMatch(JSON_ELEMENT, new TokenItemMatch(MCFunction.COMMA), true));
                g.append(new TokenItemMatch(MCFunction.BRACE,"]"));
                JSON_ELEMENT.add(g);
            }
            JSON_ELEMENT.add(new TokenItemMatch(MCFunction.STRING_LITERAL));
            JSON_ELEMENT.add(REAL_NUMBER);
            JSON_ELEMENT.add(BOOLEAN);

            TEXT_COMPONENT.add(JSON_ELEMENT);
        }

        {
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.BRACE,"{"));
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenGroupMatch().append(POSSIBLE_STRING).setName("NBT_KEY"));
                    g2.append(new TokenItemMatch(MCFunction.COLON));
                    g2.append(NBT_VALUE);
                    g.append(new TokenListMatch(g2, new TokenItemMatch(MCFunction.COMMA), true));
                }
                g.append(new TokenItemMatch(MCFunction.BRACE,"}"));
                NBT_VALUE.add(g);
                NBT_COMPOUND.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.BRACE,"["));
                g.append(new TokenListMatch(NBT_VALUE, new TokenItemMatch(MCFunction.COMMA), true));
                g.append(new TokenItemMatch(MCFunction.BRACE,"]"));
                NBT_VALUE.add(g);
            }
            NBT_VALUE.add(POSSIBLE_STRING);
            NBT_VALUE.add(TYPED_NUMBER);
            NBT_VALUE.add(BOOLEAN);
            NBT_VALUE.add(NBT_VALUE);
        }

        {
            GAMEMODE.add(new TokenItemMatch(null, "survival"));
            GAMEMODE.add(new TokenItemMatch(null, "creative"));
            GAMEMODE.add(new TokenItemMatch(null, "adventure"));
            GAMEMODE.add(new TokenItemMatch(null, "spectator"));

            SORTING.add(new TokenItemMatch(null, "nearest"));
            SORTING.add(new TokenItemMatch(null, "farthest"));
            SORTING.add(new TokenItemMatch(null, "arbitrary"));
            SORTING.add(new TokenItemMatch(null, "random"));

            BOOLEAN.add(new TokenItemMatch(null, "true"));
            BOOLEAN.add(new TokenItemMatch(null, "false"));
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(MCFunction.SELECTOR_HEADER));

            {
                TokenGroupMatch g2 = new TokenGroupMatch(true);
                g2.append(new TokenItemMatch(MCFunction.BRACE, "["));
                g2.append(new TokenListMatch(SELECTOR_ARGUMENT, new TokenItemMatch(MCFunction.COMMA), true));
                g2.append(new TokenItemMatch(MCFunction.BRACE, "]"));
                g.append(g2);
            }

            SELECTOR.add(g);
            ENTITY.add(SELECTOR);
        }

        {
            INTEGER_NUMBER_RANGE.add(INTEGER_NUMBER);
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(INTEGER_NUMBER);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenGroupMatch(true).append(INTEGER_NUMBER));
                INTEGER_NUMBER_RANGE.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(INTEGER_NUMBER);
                INTEGER_NUMBER_RANGE.add(g);
            }

            REAL_NUMBER_RANGE.add(REAL_NUMBER);
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

            g.append(new TokenGroupMatch().setName("INTEGER_ARGUMENT_VALUE").append(s));
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

            g.append(new TokenGroupMatch().setName("REAL_NUMBER_RANGE_ARGUMENT_VALUE").append(s));
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(REAL_NUMBER_RANGE);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Integer Number Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("INTEGER_NUMBER_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "limit"));

            g.append(new TokenGroupMatch().setName("INTEGER_RANGE_ARGUMENT_VALUE").append(s));
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(INTEGER_NUMBER);

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

            g.append(new TokenGroupMatch().setName("REAL_NUMBER_ARGUMENT_VALUE").append(s));
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
            s2.add(new TokenGroupMatch().append(new TokenItemMatch(null, "!", true)).append(GAMEMODE));

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
            s2.add(SORTING);

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            TokenGroupMatch cmd = new TokenGroupMatch();
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "say")).setName("COMMAND_HEADER"));
            cmd.append(SELECTOR);

            COMMAND.add(cmd);
        }

        //advancement command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("ADVANCEMENT_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "advancement")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("ADVANCEMENT_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "grant"));
            action.add(new TokenItemMatch(null, "revoke"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(ENTITY);

            {
                TokenStructureMatch following = new TokenStructureMatch("ADVANCEMENT_LIMITER");
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    g.append(new TokenItemMatch(null, "everything"));
                    following.add(g);
                }
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    TokenStructureMatch s = new TokenStructureMatch("ADVANCEMENT_LIMITER_KEYWORD");
                    s.add(new TokenItemMatch(null, "from"));
                    s.add(new TokenItemMatch(null, "until"));
                    s.add(new TokenItemMatch(null, "through"));
                    s.add(new TokenItemMatch(null, "only"));
                    g.append(new TokenGroupMatch().setName("COMMAND_NODE").append(s));

                    g.append(RESOURCE_LOCATION);

                    following.add(g);
                }

                cmd.append(following);
            }

            COMMAND.add(cmd);
        }

        //bossbar command
        {
            //create
            TokenGroupMatch cmd = new TokenGroupMatch().setName("BOSSBAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "bossbar")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("BOSSBAR_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "create"));
            action.add(new TokenItemMatch(null, "create"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(RESOURCE_LOCATION);
            cmd.append(TEXT_COMPONENT);

            COMMAND.add(cmd);
        }
        {
            //get
            TokenGroupMatch cmd = new TokenGroupMatch().setName("BOSSBAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "bossbar")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("BOSSBAR_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "get"));
            action.add(new TokenItemMatch(null, "get"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(RESOURCE_LOCATION);

            TokenStructureMatch what = new TokenStructureMatch("BOSSBAR_PROPERTY");
            what.add(new TokenItemMatch(null, "max"));
            what.add(new TokenItemMatch(null, "players"));
            what.add(new TokenItemMatch(null, "value"));
            what.add(new TokenItemMatch(null, "visible"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(what));

            COMMAND.add(cmd);
        }
        {
            //list
            TokenGroupMatch cmd = new TokenGroupMatch().setName("BOSSBAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "bossbar")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("BOSSBAR_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "list"));
            action.add(new TokenItemMatch(null, "list"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            COMMAND.add(cmd);
        }
        {
            //remove
            TokenGroupMatch cmd = new TokenGroupMatch().setName("BOSSBAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "bossbar")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("BOSSBAR_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "remove"));
            action.add(new TokenItemMatch(null, "remove"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(RESOURCE_LOCATION);

            COMMAND.add(cmd);
        }
        {
            //set
            TokenGroupMatch cmd = new TokenGroupMatch().setName("BOSSBAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "bossbar")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("BOSSBAR_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "set"));
            action.add(new TokenItemMatch(null, "set"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(RESOURCE_LOCATION);

            TokenStructureMatch clause = new TokenStructureMatch("BOSSBAR_PROPERTY_CLAUSE");

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "color").setName("COMMAND_NODE"));

                TokenStructureMatch color = new TokenStructureMatch("BOSSBAR_COLOR");
                color.add(new TokenItemMatch(null, "blue"));
                color.add(new TokenItemMatch(null, "green"));
                color.add(new TokenItemMatch(null, "pink"));
                color.add(new TokenItemMatch(null, "purple"));
                color.add(new TokenItemMatch(null, "red"));
                color.add(new TokenItemMatch(null, "white"));
                color.add(new TokenItemMatch(null, "yellow"));
                g.append(color);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "max").setName("COMMAND_NODE"));

                g.append(INTEGER_NUMBER);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "name").setName("COMMAND_NODE"));

                g.append(TEXT_COMPONENT);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "players").setName("COMMAND_NODE"));

                g.append(ENTITY);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "style").setName("COMMAND_NODE"));

                TokenStructureMatch style = new TokenStructureMatch("BOSSBAR_STYLE");
                style.add(new TokenItemMatch(null, "notched_6"));
                style.add(new TokenItemMatch(null, "notched_10"));
                style.add(new TokenItemMatch(null, "notched_12"));
                style.add(new TokenItemMatch(null, "notched_20"));
                style.add(new TokenItemMatch(null, "progress"));
                g.append(style);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "value").setName("COMMAND_NODE"));

                g.append(INTEGER_NUMBER);

                clause.add(g);
            }

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(null, "visible").setName("COMMAND_NODE"));

                g.append(BOOLEAN);

                clause.add(g);
            }

            cmd.append(clause);

            COMMAND.add(cmd);
        }

        //clear command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("CLEAR_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "clear")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);

            {
                TokenGroupMatch g = new TokenGroupMatch(true);
                g.append(ITEM_TAGGED);
                g.append(new TokenGroupMatch(true).append(INTEGER_NUMBER));

                cmd.append(g);
            }

            COMMAND.add(cmd);
        }

        //clone command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("CLONE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "clone")).setName("COMMAND_HEADER"));

            cmd.append(COORDINATE_SET);
            cmd.append(COORDINATE_SET);
            cmd.append(COORDINATE_SET);

            TokenStructureMatch OVERLAP_POLICY = new TokenStructureMatch("OVERLAP_POLICY");
            OVERLAP_POLICY.add(new TokenItemMatch(null, "normal"));
            OVERLAP_POLICY.add(new TokenItemMatch(null, "force"));
            OVERLAP_POLICY.add(new TokenItemMatch(null, "move"));

            {
                TokenStructureMatch following = new TokenStructureMatch("CLONE_PARAMETERS");
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    TokenStructureMatch s = new TokenStructureMatch("CLONE_PARAMETER_KEYWORD");
                    s.add(new TokenItemMatch(null, "filtered"));
                    s.add(new TokenItemMatch(null, "filtered"));
                    g.append(new TokenGroupMatch().setName("COMMAND_NODE").append(s));
                    g.append(BLOCK_TAGGED);
                    g.append(new TokenGroupMatch(true).append(OVERLAP_POLICY));
                    following.add(g);
                }
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    TokenStructureMatch s = new TokenStructureMatch("CLONE_PARAMETER_KEYWORD");
                    s.add(new TokenItemMatch(null, "replace"));
                    s.add(new TokenItemMatch(null, "masked"));
                    g.append(new TokenGroupMatch().setName("COMMAND_NODE").append(s));
                    g.append(new TokenGroupMatch(true).append(OVERLAP_POLICY));
                    following.add(g);
                }

                cmd.append(new TokenGroupMatch(true).append(following));
            }

            COMMAND.add(cmd);
        }

        //tag command
        {
            //add/remove
            TokenGroupMatch cmd = new TokenGroupMatch().setName("TAG_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "tag")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);

            TokenStructureMatch action = new TokenStructureMatch("TAG_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "add"));
            action.add(new TokenItemMatch(null, "remove"));

            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(ANY_STRING);

            COMMAND.add(cmd);
        }
        {
            //list
            TokenGroupMatch cmd = new TokenGroupMatch().setName("TAG_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "tag")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);

            TokenStructureMatch action = new TokenStructureMatch("TAG_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "list"));
            action.add(new TokenItemMatch(null, "list"));

            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            COMMAND.add(cmd);
        }
    }
}
