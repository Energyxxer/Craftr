package com.energyxxer.craftrlang.compiler.parsing;

import com.energyxxer.commodore.defpacks.DefinitionBlueprint;
import com.energyxxer.commodore.defpacks.DefinitionPack;
import com.energyxxer.commodore.standard.StandardDefinitionPacks;
import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.mcfunction.MCFunction;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.*;

import java.util.HashMap;

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

    public static final TokenStructureMatch COLOR = new TokenStructureMatch("COLOR");

    public static final TokenStructureMatch BLOCK = new TokenStructureMatch("BLOCK");
    public static final TokenStructureMatch BLOCK_TAGGED = new TokenStructureMatch("BLOCK_TAGGED");
    public static final TokenStructureMatch ITEM = new TokenStructureMatch("ITEM");
    public static final TokenStructureMatch ITEM_TAGGED = new TokenStructureMatch("ITEM_TAGGED");

    public static final TokenStructureMatch PARTICLE = new TokenStructureMatch("PARTICLE");

    public static final TokenStructureMatch NBT_COMPOUND = new TokenStructureMatch("NBT_COMPOUND");
    public static final TokenStructureMatch NBT_VALUE = new TokenStructureMatch("NBT_VALUE");

    public static final TokenStructureMatch NBT_PATH = new TokenStructureMatch("NBT_PATH");
    public static final TokenStructureMatch NUMERIC_DATA_TYPE = new TokenStructureMatch("NUMERIC_DATA_TYPE");

    public static final TokenStructureMatch ENTITY = new TokenStructureMatch("ENTITY");

    public static final TokenStructureMatch ANCHOR = new TokenStructureMatch("ANCHOR");

    public static final TokenStructureMatch SINGLE_COORDINATE = new TokenStructureMatch("SINGLE_COORDINATE");
    public static final TokenStructureMatch ABSOLUTE_COORDINATE = new TokenStructureMatch("ABSOLUTE_COORDINATE");
    public static final TokenStructureMatch RELATIVE_COORDINATE = new TokenStructureMatch("RELATIVE_COORDINATE");
    public static final TokenStructureMatch LOCAL_COORDINATE = new TokenStructureMatch("LOCAL_COORDINATE");
    public static final TokenStructureMatch MIXABLE_COORDINATE = new TokenStructureMatch("MIXABLE_COORDINATE");
    public static final TokenStructureMatch COORDINATE_SET = new TokenStructureMatch("COORDINATE_SET");
    public static final TokenStructureMatch TWO_COORDINATE_SET = new TokenStructureMatch("TWO_COORDINATE_SET");

    public static final TokenStructureMatch SINGLE_ROTATION = new TokenStructureMatch("SINGLE_ROTATION");
    public static final TokenStructureMatch ROTATION_SET = new TokenStructureMatch("ROTATION_SET");

    public static final TokenStructureMatch SELECTOR = new TokenStructureMatch("SELECTOR");
    public static final TokenStructureMatch SELECTOR_ARGUMENT = new TokenStructureMatch("SELECTOR_ARGUMENT");

    public static final TokenStructureMatch INTEGER_NUMBER_RANGE = new TokenStructureMatch("INTEGER_NUMBER_RANGE");
    public static final TokenStructureMatch REAL_NUMBER_RANGE = new TokenStructureMatch("REAL_NUMBER_RANGE");

    public static final TokenStructureMatch BLOCK_ID = new TokenStructureMatch("BLOCK_ID");
    public static final TokenStructureMatch ITEM_ID = new TokenStructureMatch("ITEM_ID");
    public static final TokenStructureMatch ENTITY_ID = new TokenStructureMatch("ENTITY_ID");
    public static final TokenStructureMatch EFFECT_ID = new TokenStructureMatch("EFFECT_ID");
    public static final TokenStructureMatch PARTICLE_ID = new TokenStructureMatch("PARTICLE_ID");
    public static final TokenStructureMatch ENCHANTMENT_ID = new TokenStructureMatch("ENCHANTMENT_ID");
    public static final TokenStructureMatch DIMENSION_ID = new TokenStructureMatch("DIMENSION_ID");

    public static final TokenStructureMatch GAMEMODE = new TokenStructureMatch("GAMEMODE");
    public static final TokenStructureMatch STRUCTURE = new TokenStructureMatch("STRUCTURE");
    public static final TokenStructureMatch DIFFICULTY = new TokenStructureMatch("DIFFICULTY");
    public static final TokenStructureMatch SORTING = new TokenStructureMatch("SORTING");

    private static final TokenGlue GLUE = new TokenGlue(new TokenItemMatch(MCFunction.NEWLINE));

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
            g.append(GLUE);
            g.append(new TokenItemMatch(MCFunction.COLON));

            NAMESPACE.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenGroupMatch(true).append(NAMESPACE).append(GLUE));
            g.append(new TokenListMatch(new TokenItemMatch(TokenType.UNKNOWN), new TokenGroupMatch().append(GLUE).append(new TokenItemMatch(null, "/")).append(GLUE)));

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
            TokenGroupMatch g = new TokenGroupMatch().setName("CONCRETE_RESOURCE");
            g.append(new TokenGroupMatch().append(BLOCK_ID).setName("RESOURCE_NAME"));
            g.append(new TokenGroupMatch(true).append(GLUE).append(BLOCKSTATE));
            g.append(new TokenGroupMatch(true).append(GLUE).append(NBT_COMPOUND));
            BLOCK.add(g);
            BLOCK_TAGGED.add(BLOCK);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch().setName("ABSTRACT_RESOURCE");
            g.append(new TokenGroupMatch().append(new TokenItemMatch(null, "#").setName("TAG_HEADER")).append(GLUE).append(RESOURCE_LOCATION).setName("RESOURCE_NAME"));
            g.append(new TokenGroupMatch(true).append(GLUE).append(BLOCKSTATE));
            g.append(new TokenGroupMatch(true).append(GLUE).append(NBT_COMPOUND));
            BLOCK_TAGGED.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch().setName("CONCRETE_RESOURCE");
            g.append(new TokenGroupMatch().append(ITEM_ID).setName("RESOURCE_NAME"));
            g.append(new TokenGroupMatch(true).append(GLUE).append(NBT_COMPOUND));
            ITEM.add(g);
            ITEM_TAGGED.add(ITEM);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch().setName("ABSTRACT_RESOURCE");
            g.append(new TokenGroupMatch().append(new TokenItemMatch(null, "#").setName("TAG_HEADER")).append(GLUE).append(RESOURCE_LOCATION).setName("RESOURCE_NAME"));
            g.append(new TokenGroupMatch(true).append(GLUE).append(NBT_COMPOUND));
            ITEM_TAGGED.add(g);
        }

        {

            TokenStructureMatch LOCAL_COORDINATE = new TokenStructureMatch("LOCAL_COORDINATE");
            LOCAL_COORDINATE.add(new TokenGroupMatch().append(new TokenItemMatch(MCFunction.CARET).setName("CARET")).append(new TokenGroupMatch(true).append(GLUE).append(REAL_NUMBER)));

            TokenStructureMatch ABSOLUTE_COORDINATE = new TokenStructureMatch("ABSOLUTE_COORDINATE");
            ABSOLUTE_COORDINATE.add(REAL_NUMBER);

            TokenStructureMatch RELATIVE_COORDINATE = new TokenStructureMatch("RELATIVE_COORDINATE");
            RELATIVE_COORDINATE.add(new TokenGroupMatch().append(new TokenItemMatch(MCFunction.TILDE).setName("TILDE")).append(new TokenGroupMatch(true).append(GLUE).append(REAL_NUMBER)));

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
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenGroupMatch().append(REAL_NUMBER).setName("RED_COLOR"));
            g.append(new TokenGroupMatch().append(REAL_NUMBER).setName("GREEN_COLOR"));
            g.append(new TokenGroupMatch().append(REAL_NUMBER).setName("BLUE_COLOR"));

            COLOR.add(g);
        }

        {
            TokenStructureMatch ABSOLUTE_ROTATION = new TokenStructureMatch("ABSOLUTE_ROTATION");
            ABSOLUTE_ROTATION.add(REAL_NUMBER);

            TokenStructureMatch RELATIVE_ROTATION = new TokenStructureMatch("RELATIVE_ROTATION");
            RELATIVE_ROTATION.add(new TokenGroupMatch().append(new TokenItemMatch(MCFunction.TILDE).setName("TILDE")).append(new TokenGroupMatch(true).append(GLUE).append(REAL_NUMBER)));

            SINGLE_ROTATION.add(ABSOLUTE_ROTATION);
            SINGLE_ROTATION.add(RELATIVE_ROTATION);

            TokenGroupMatch g = new TokenGroupMatch();
            g.append(SINGLE_ROTATION);
            g.append(SINGLE_ROTATION);
            ROTATION_SET.add(g);
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
            TokenStructureMatch NBT_PATH_NODE = new TokenStructureMatch("NBT_PATH_NODE");

            TokenGroupMatch NBT_PATH_KEY = new TokenGroupMatch().setName("NBT_PATH_KEY");
            NBT_PATH_KEY.append(GLUE);
            NBT_PATH_KEY.append(new TokenItemMatch(MCFunction.DOT).setName("NBT_PATH_SEPARATOR"));
            NBT_PATH_KEY.append(GLUE);
            NBT_PATH_KEY.append(new TokenItemMatch(TokenType.UNKNOWN).setName("NBT_PATH_KEY_LABEL"));
            NBT_PATH_NODE.add(NBT_PATH_KEY);

            TokenGroupMatch NBT_PATH_INDEX = new TokenGroupMatch().setName("NBT_PATH_INDEX");
            NBT_PATH_INDEX.append(GLUE);
            NBT_PATH_INDEX.append(new TokenItemMatch(MCFunction.BRACE, "["));
            NBT_PATH_INDEX.append(GLUE);
            NBT_PATH_INDEX.append(INTEGER_NUMBER);
            NBT_PATH_INDEX.append(GLUE);
            NBT_PATH_INDEX.append(new TokenItemMatch(MCFunction.BRACE, "]"));
            NBT_PATH_NODE.add(NBT_PATH_INDEX);

            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenGroupMatch().append(new TokenItemMatch(TokenType.UNKNOWN).setName("NBT_PATH_KEY_LABEL")).setName("NBT_PATH_KEY"));
            g.append(new TokenListMatch(NBT_PATH_NODE, GLUE, true));

            NBT_PATH.add(g);
        }

        {

            SORTING.add(new TokenItemMatch(null, "nearest"));
            SORTING.add(new TokenItemMatch(null, "farthest"));
            SORTING.add(new TokenItemMatch(null, "arbitrary"));
            SORTING.add(new TokenItemMatch(null, "random"));

            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "byte"));
            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "double"));
            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "float"));
            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "int"));
            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "long"));
            NUMERIC_DATA_TYPE.add(new TokenItemMatch(null, "short"));

            BOOLEAN.add(new TokenItemMatch(null, "true"));
            BOOLEAN.add(new TokenItemMatch(null, "false"));

            ANCHOR.add(new TokenItemMatch(null, "feet"));
            ANCHOR.add(new TokenItemMatch(null, "eyes"));

            DefinitionPack defpack = StandardDefinitionPacks.MINECRAFT_J_1_13;

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.STRUCTURE)) {
                STRUCTURE.add(new TokenItemMatch(null, def.getName()));
            }

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.DIFFICULTY)) {
                DIFFICULTY.add(new TokenItemMatch(null, def.getName()));
            }

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.GAMEMODE)) {
                GAMEMODE.add(new TokenItemMatch(null, def.getName()));
            }

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.DIMENSION)) {
                DIMENSION_ID.add(new TokenItemMatch(null, def.getName()));
            }

            HashMap<String, TokenStructureMatch> namespaceGroups = new HashMap<>();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.BLOCK)) {

                TokenStructureMatch s = namespaceGroups.get(def.getNamespace());

                if(s == null) {
                    TokenGroupMatch g = new TokenGroupMatch().setName("BLOCK_ID");

                    TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                    ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                    ns.append(new TokenItemMatch(MCFunction.COLON));

                    g.append(ns);

                    s = new TokenStructureMatch("BLOCK_NAME");
                    g.append(s);

                    namespaceGroups.put(def.getNamespace(), s);

                    BLOCK_ID.add(g);
                }

                s.add(new TokenItemMatch(TokenType.UNKNOWN, def.getName()));
            }

            namespaceGroups.clear();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.ITEM)) {

                TokenStructureMatch s = namespaceGroups.get(def.getNamespace());

                if(s == null) {
                    TokenGroupMatch g = new TokenGroupMatch().setName("ITEM_ID");

                    TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                    ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                    ns.append(new TokenItemMatch(MCFunction.COLON));

                    g.append(ns);

                    s = new TokenStructureMatch("ITEM_NAME");
                    g.append(s);

                    namespaceGroups.put(def.getNamespace(), s);

                    ITEM_ID.add(g);
                }

                s.add(new TokenItemMatch(TokenType.UNKNOWN, def.getName()));
            }

            namespaceGroups.clear();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.ENTITY)) {

                TokenStructureMatch s = namespaceGroups.get(def.getNamespace());

                if(s == null) {
                    TokenGroupMatch g = new TokenGroupMatch().setName("ENTITY_ID");

                    TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                    ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                    ns.append(new TokenItemMatch(MCFunction.COLON));

                    g.append(ns);

                    s = new TokenStructureMatch("ENTITY_NAME");
                    g.append(s);

                    namespaceGroups.put(def.getNamespace(), s);

                    ENTITY_ID.add(g);
                }

                s.add(new TokenItemMatch(TokenType.UNKNOWN, def.getName()));
            }

            namespaceGroups.clear();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.EFFECT)) {

                TokenStructureMatch s = namespaceGroups.get(def.getNamespace());

                if(s == null) {
                    TokenGroupMatch g = new TokenGroupMatch().setName("EFFECT_ID");

                    TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                    ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                    ns.append(new TokenItemMatch(MCFunction.COLON));

                    g.append(ns);

                    s = new TokenStructureMatch("EFFECT_NAME");
                    g.append(s);

                    namespaceGroups.put(def.getNamespace(), s);

                    EFFECT_ID.add(g);
                }

                s.add(new TokenItemMatch(TokenType.UNKNOWN, def.getName()));
            }

            namespaceGroups.clear();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.ENCHANTMENT)) {

                TokenStructureMatch s = namespaceGroups.get(def.getNamespace());

                if(s == null) {
                    TokenGroupMatch g = new TokenGroupMatch().setName("ENCHANTMENT_ID");

                    TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                    ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                    ns.append(new TokenItemMatch(MCFunction.COLON));

                    g.append(ns);

                    s = new TokenStructureMatch("ENCHANTMENT_NAME");
                    g.append(s);

                    namespaceGroups.put(def.getNamespace(), s);

                    ENCHANTMENT_ID.add(g);
                }

                s.add(new TokenItemMatch(TokenType.UNKNOWN, def.getName()));
            }

            namespaceGroups.clear();

            for(DefinitionBlueprint def : defpack.getBlueprints(DefinitionPack.DefinitionCategory.PARTICLE)) {
                TokenGroupMatch g = new TokenGroupMatch().setName("PARTICLE_ID");

                TokenGroupMatch ns = new TokenGroupMatch(def.getNamespace().equals("minecraft")).setName("NAMESPACE");
                ns.append(new TokenItemMatch(TokenType.UNKNOWN, def.getNamespace()));
                ns.append(new TokenItemMatch(MCFunction.COLON));

                g.append(ns);

                g.append(new TokenItemMatch(TokenType.UNKNOWN, def.getName()).setName("PARTICLE_NAME"));

                PARTICLE_ID.add(g);

                TokenGroupMatch g2 = new TokenGroupMatch();

                g2.append(g);

                TokenGroupMatch argsGroup = new TokenGroupMatch().setName("PARTICLE_ARGUMENTS");

                String allArgs = def.getProperties().get("argument");
                if(!allArgs.equals("none")) {
                    String[] args = allArgs.split("-");
                    for(String arg : args) {
                        switch(arg) {
                            case "int": {
                                argsGroup.append(INTEGER_NUMBER);
                                break;
                            }
                            case "double": {
                                argsGroup.append(REAL_NUMBER);
                                break;
                            }
                            case "color": {
                                argsGroup.append(COLOR);
                                break;
                            }
                            case "block": {
                                argsGroup.append(BLOCK);
                                break;
                            }
                            case "item": {
                                argsGroup.append(ITEM);
                                break;
                            }
                            default: {
                                System.err.println("Invalid particle argument type '" + arg + "', could not be added to .mcfunction particle production");
                            }
                        }
                    }
                }

                g2.append(argsGroup);

                PARTICLE.add(g2);
            }

            namespaceGroups.clear();
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(MCFunction.SELECTOR_HEADER));

            {
                TokenGroupMatch g2 = new TokenGroupMatch(true);
                g2.append(GLUE);
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
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenGroupMatch(true).append(GLUE).append(INTEGER_NUMBER));
                INTEGER_NUMBER_RANGE.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
                g.append(INTEGER_NUMBER);
                INTEGER_NUMBER_RANGE.add(g);
            }

            REAL_NUMBER_RANGE.add(REAL_NUMBER);
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(REAL_NUMBER);
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(new TokenGroupMatch(true).append(GLUE).append(REAL_NUMBER));
                REAL_NUMBER_RANGE.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
                g.append(new TokenItemMatch(MCFunction.DOT));
                g.append(GLUE);
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
            g.append(GLUE);
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
            g.append(GLUE);
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
            g.append(GLUE);
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
            g.append(GLUE);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(REAL_NUMBER);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //String Arguments
            TokenGroupMatch g = new TokenGroupMatch().setName("STRING_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "name"));
            s.add(new TokenItemMatch(null, "tag"));
            s.add(new TokenItemMatch(null, "team"));

            g.append(s);
            g.append(GLUE);
            g.append(new TokenItemMatch(MCFunction.EQUALS));
            g.append(new TokenItemMatch(null, "!", true));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE", true);
            s2.add(POSSIBLE_STRING);

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Gamemode argument
            TokenGroupMatch g = new TokenGroupMatch().setName("GAMEMODE_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "gamemode"));

            g.append(s);
            g.append(GLUE);
            g.append(new TokenItemMatch(MCFunction.EQUALS));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE");
            s2.add(new TokenGroupMatch().append(new TokenItemMatch(null, "!", true)).append(GAMEMODE));

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Type argument
            TokenGroupMatch g = new TokenGroupMatch().setName("TYPE_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "type"));

            g.append(s);
            g.append(GLUE);
            g.append(new TokenItemMatch(MCFunction.EQUALS));

            TokenStructureMatch s2 = new TokenStructureMatch("SELECTOR_ARGUMENT_VALUE");
            s2.add(new TokenGroupMatch().append(new TokenItemMatch(null, "!", true)).append(ENTITY_ID));

            g.append(s2);

            SELECTOR_ARGUMENT.add(g);
        }

        {
            //Sort argument
            TokenGroupMatch g = new TokenGroupMatch().setName("SORT_ARGUMENT");

            TokenStructureMatch s = new TokenStructureMatch("SELECTOR_ARGUMENT_KEY");
            s.add(new TokenItemMatch(null, "sort"));

            g.append(s);
            g.append(GLUE);
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

        //data command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DATA_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "data")).setName("COMMAND_HEADER"));

            TokenStructureMatch ENTITY_OR_BLOCK = new TokenStructureMatch("ENTITY_OR_BLOCK");
            {
                TokenGroupMatch g = new TokenGroupMatch().setName("DATA_BLOCK");
                g.append(new TokenItemMatch(null, "block").setName("COMMAND_NODE"));
                g.append(COORDINATE_SET);
                ENTITY_OR_BLOCK.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch().setName("DATA_ENTITY");
                g.append(new TokenItemMatch(null, "entity").setName("COMMAND_NODE"));
                g.append(ENTITY);
                ENTITY_OR_BLOCK.add(g);
            }

            {
                TokenStructureMatch following = new TokenStructureMatch("DATA_BRANCH");
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    g.append(new TokenGroupMatch().append(new TokenItemMatch(null, "get").setName("DATA_MODE")).setName("COMMAND_NODE"));
                    g.append(ENTITY_OR_BLOCK);
                    g.append(new TokenGroupMatch(true).append(NBT_PATH).append(new TokenGroupMatch(true).append(REAL_NUMBER)));
                    following.add(g);
                }
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    g.append(new TokenGroupMatch().append(new TokenItemMatch(null, "remove").setName("DATA_MODE")).setName("COMMAND_NODE"));
                    g.append(ENTITY_OR_BLOCK);
                    g.append(NBT_PATH);
                    following.add(g);
                }
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    g.append(new TokenGroupMatch().append(new TokenItemMatch(null, "merge").setName("DATA_MODE")).setName("COMMAND_NODE"));
                    g.append(ENTITY_OR_BLOCK);
                    g.append(NBT_COMPOUND);
                    following.add(g);
                }

                cmd.append(following);
            }

            COMMAND.add(cmd);
        }

        //datapack command
        {
            //disable
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DATAPACK_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "datapack")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("DATAPACK_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "disable"));
            action.add(new TokenItemMatch(null, "disable"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(POSSIBLE_STRING);

            COMMAND.add(cmd);
        }
        {
            //enable
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DATAPACK_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "datapack")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("DATAPACK_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "enable"));
            action.add(new TokenItemMatch(null, "enable"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            cmd.append(POSSIBLE_STRING);

            {
                TokenStructureMatch following = new TokenStructureMatch("DATAPACK_BRANCH");
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    TokenStructureMatch order = new TokenStructureMatch("DATAPACK_ORDER");
                    order.add(new TokenItemMatch(null, "after").setName("COMMAND_NODE"));
                    order.add(new TokenItemMatch(null, "before").setName("COMMAND_NODE"));
                    g.append(order);
                    g.append(POSSIBLE_STRING);
                    following.add(g);
                }
                {
                    TokenGroupMatch g = new TokenGroupMatch();
                    TokenStructureMatch order = new TokenStructureMatch("DATAPACK_ORDER");
                    order.add(new TokenItemMatch(null, "first").setName("COMMAND_NODE"));
                    order.add(new TokenItemMatch(null, "last").setName("COMMAND_NODE"));
                    g.append(order);
                    following.add(g);
                }
                cmd.append(new TokenGroupMatch(true).append(following));
            }

            COMMAND.add(cmd);
        }
        {
            //list
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DATAPACK_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "datapack")).setName("COMMAND_HEADER"));

            TokenStructureMatch action = new TokenStructureMatch("DATAPACK_COMMAND_ACTION");
            action.add(new TokenItemMatch(null, "list"));
            action.add(new TokenItemMatch(null, "list"));
            cmd.append(new TokenGroupMatch().setName("COMMAND_NODE").append(action));

            {
                TokenStructureMatch DATAPACK_FILTER = new TokenStructureMatch("DATAPACK_FILTER");
                DATAPACK_FILTER.add(new TokenItemMatch(null, "available"));
                DATAPACK_FILTER.add(new TokenItemMatch(null, "enabled"));
                cmd.append(new TokenGroupMatch(true).setName("COMMAND_NODE").append(DATAPACK_FILTER));
            }

            COMMAND.add(cmd);
        }

        //defaultgamemode command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DEFAULTGAMEMODE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "defaultgamemode")).setName("COMMAND_HEADER"));

            cmd.append(GAMEMODE);

            COMMAND.add(cmd);
        }

        //difficulty command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("DIFFICULTY_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "difficulty")).setName("COMMAND_HEADER"));

            cmd.append(new TokenGroupMatch(true).append(DIFFICULTY));

            COMMAND.add(cmd);
        }

        //effect command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("EFFECT_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "effect")).setName("COMMAND_HEADER"));

            TokenStructureMatch following = new TokenStructureMatch("EFFECT_BRANCH");
            {
                TokenGroupMatch g = new TokenGroupMatch();
                TokenStructureMatch action = new TokenStructureMatch("EFFECT_ACTION");
                action.add(new TokenItemMatch(null, "give").setName("COMMAND_NODE"));
                action.add(new TokenItemMatch(null, "give").setName("COMMAND_NODE"));
                g.append(action);

                g.append(ENTITY);
                g.append(EFFECT_ID);

                g.append(new TokenGroupMatch(true).append(INTEGER_NUMBER).append(new TokenGroupMatch(true).append(INTEGER_NUMBER).append(new TokenGroupMatch(true).append(BOOLEAN))));

                following.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                TokenStructureMatch action = new TokenStructureMatch("EFFECT_ACTION");
                action.add(new TokenItemMatch(null, "clear").setName("COMMAND_NODE"));
                action.add(new TokenItemMatch(null, "clear").setName("COMMAND_NODE"));
                g.append(action);

                g.append(ENTITY);
                g.append(new TokenGroupMatch(true).append(EFFECT_ID));

                following.add(g);
            }

            cmd.append(following);

            COMMAND.add(cmd);
        }

        //enchant command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("ENCHANT_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "enchant")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);
            cmd.append(ENCHANTMENT_ID);
            cmd.append(new TokenGroupMatch(true).append(INTEGER_NUMBER));

            COMMAND.add(cmd);
        }

        //EXECUTE COMMAND
        {
            TokenStructureMatch EXECUTE_SUBCOMMAND = new TokenStructureMatch("EXECUTE_SUBCOMMAND");

            {
                //align
                TokenGroupMatch g = new TokenGroupMatch().setName("ALIGN_SUBCOMMAND");
                g.append(new TokenItemMatch(null, "align").setName("SUBCOMMAND_HEADER"));
                g.append(new TokenItemMatch(TokenType.UNKNOWN));
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                //anchored
                TokenGroupMatch g = new TokenGroupMatch().setName("ANCHORED_SUBCOMMAND");
                g.append(new TokenItemMatch(null, "anchored").setName("SUBCOMMAND_HEADER"));
                g.append(ANCHOR);
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                //as
                TokenGroupMatch g = new TokenGroupMatch().setName("AS_SUBCOMMAND");
                g.append(new TokenItemMatch(null, "as").setName("SUBCOMMAND_HEADER"));
                g.append(ENTITY);
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                //at
                TokenGroupMatch g = new TokenGroupMatch().setName("AT_SUBCOMMAND");
                g.append(new TokenItemMatch(null, "at").setName("SUBCOMMAND_HEADER"));
                g.append(ENTITY);
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                //facing
                TokenGroupMatch g = new TokenGroupMatch().setName("FACING_SUBCOMMAND");
                g.append(new TokenItemMatch(null, "facing").setName("SUBCOMMAND_HEADER"));

                TokenStructureMatch FACING_CLAUSE = new TokenStructureMatch("FACING_CLAUSE");
                FACING_CLAUSE.add(new TokenGroupMatch().append(new TokenItemMatch(null, "entity").setName("SUBCOMMAND_NODE")).append(ENTITY).append(ANCHOR));
                FACING_CLAUSE.add(new TokenGroupMatch().append(COORDINATE_SET));

                g.append(FACING_CLAUSE);
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                //conditional (if/unless)
                TokenGroupMatch g = new TokenGroupMatch().setName("CONDITIONAL_SUBCOMMAND");
                g.append(new TokenStructureMatch("CONDITIONAL_KEYWORD")
                        .add(new TokenItemMatch(null, "if").setName("SUBCOMMAND_HEADER"))
                        .add(new TokenItemMatch(null, "unless").setName("SUBCOMMAND_HEADER")));

                TokenStructureMatch CONDITIONAL_CLAUSE = new TokenStructureMatch("CONDITIONAL_CLAUSE");
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenItemMatch(null, "block").setName("SUBCOMMAND_NODE"));
                    g2.append(COORDINATE_SET);
                    g2.append(BLOCK_TAGGED);
                    CONDITIONAL_CLAUSE.add(g2);
                }
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenItemMatch(null, "blocks").setName("SUBCOMMAND_NODE"));
                    g2.append(COORDINATE_SET);
                    g2.append(COORDINATE_SET);
                    g2.append(COORDINATE_SET);
                    g2.append(new TokenStructureMatch("BLOCK_POLICY").add(new TokenItemMatch(null, "all")).add(new TokenItemMatch(null, "masked")));
                    CONDITIONAL_CLAUSE.add(g2);
                }
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenItemMatch(null, "entity").setName("SUBCOMMAND_NODE"));
                    g2.append(ENTITY);
                    CONDITIONAL_CLAUSE.add(g2);
                }
                {
                    TokenGroupMatch g2 = new TokenGroupMatch();
                    g2.append(new TokenItemMatch(null, "score").setName("SUBCOMMAND_NODE"));
                    g2.append(ENTITY);
                    g2.append(ANY_STRING);

                    TokenStructureMatch COMPARISON_CLAUSE = new TokenStructureMatch("COMPARISON_CLAUSE");
                    {
                        TokenGroupMatch g3 = new TokenGroupMatch();
                        g3.append(new TokenStructureMatch("COMPARISON_OPERATOR")
                                .add(new TokenItemMatch(null, "<"))
                                .add(new TokenItemMatch(null, "<="))
                                .add(new TokenItemMatch(null, "="))
                                .add(new TokenItemMatch(null, ">"))
                                .add(new TokenItemMatch(null, ">=")));
                        g3.append(ENTITY);
                        g3.append(ANY_STRING);
                        COMPARISON_CLAUSE.add(g3);
                    }
                    {
                        TokenGroupMatch g3 = new TokenGroupMatch();
                        g3.append(new TokenItemMatch(null, "matches"));
                        g3.append(INTEGER_NUMBER_RANGE);
                        COMPARISON_CLAUSE.add(g3);
                    }
                    g2.append(COMPARISON_CLAUSE);
                    CONDITIONAL_CLAUSE.add(g2);
                }
                EXECUTE_SUBCOMMAND.add(g);
            }

            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("IN_SUBCOMMAND");
                g2.append(new TokenItemMatch(null, "in").setName("SUBCOMMAND_HEADER"));
                g2.append(DIMENSION_ID);
                EXECUTE_SUBCOMMAND.add(g2);
            }
            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("POSITIONED_SUBCOMMAND");
                g2.append(new TokenItemMatch(null, "positioned").setName("SUBCOMMAND_HEADER"));
                g2.append(new TokenStructureMatch("POSITIONED_CLAUSE")
                        .add(COORDINATE_SET)
                        .add(new TokenGroupMatch().append(new TokenItemMatch(null, "as").setName("SUBCOMMAND_NODE")).append(ENTITY)));
                EXECUTE_SUBCOMMAND.add(g2);
            }
            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("ROTATED_SUBCOMMAND");
                g2.append(new TokenItemMatch(null, "rotated").setName("SUBCOMMAND_HEADER"));
                g2.append(new TokenStructureMatch("ROTATED_CLAUSE")
                        .add(ROTATION_SET)
                        .add(new TokenGroupMatch().append(new TokenItemMatch(null, "as").setName("SUBCOMMAND_NODE")).append(ENTITY)));
                EXECUTE_SUBCOMMAND.add(g2);
            }
            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("STORE_SUBCOMMAND");
                g2.append(new TokenItemMatch(null, "store").setName("SUBCOMMAND_HEADER"));
                g2.append(new TokenStructureMatch("STORE_VALUE")
                        .add(new TokenItemMatch(null, "result").setName("SUBCOMMAND_NODE"))
                        .add(new TokenItemMatch(null, "success").setName("SUBCOMMAND_NODE")));

                TokenStructureMatch STORE_CLAUSE = new TokenStructureMatch("STORE_CLAUSE");
                {
                    TokenGroupMatch g3 = new TokenGroupMatch();
                    g3.append(new TokenStructureMatch("STORE_SLOT")
                            .add(new TokenItemMatch(null, "block").setName("SUBCOMMAND_NODE"))
                            .add(new TokenItemMatch(null, "block").setName("SUBCOMMAND_NODE")));
                    g3.append(COORDINATE_SET);
                    g3.append(NBT_PATH);
                    g3.append(NUMERIC_DATA_TYPE);
                    g3.append(REAL_NUMBER);
                    STORE_CLAUSE.add(g3);
                }
                {
                    TokenGroupMatch g3 = new TokenGroupMatch();
                    g3.append(new TokenStructureMatch("STORE_SLOT")
                            .add(new TokenItemMatch(null, "bossbar").setName("SUBCOMMAND_NODE"))
                            .add(new TokenItemMatch(null, "bossbar").setName("SUBCOMMAND_NODE")));
                    g3.append(ANY_STRING);
                    g3.append(NBT_PATH);
                    g3.append(new TokenStructureMatch("STORE_BOSSBAR_SLOT")
                            .add(new TokenItemMatch(null, "max").setName("SUBCOMMAND_NODE"))
                            .add(new TokenItemMatch(null, "value").setName("SUBCOMMAND_NODE")));
                    STORE_CLAUSE.add(g3);
                }
                {
                    TokenGroupMatch g3 = new TokenGroupMatch();
                    g3.append(new TokenStructureMatch("STORE_SLOT")
                            .add(new TokenItemMatch(null, "entity").setName("SUBCOMMAND_NODE"))
                            .add(new TokenItemMatch(null, "entity").setName("SUBCOMMAND_NODE")));
                    g3.append(ENTITY);
                    g3.append(NBT_PATH);
                    g3.append(NUMERIC_DATA_TYPE);
                    g3.append(REAL_NUMBER);
                    STORE_CLAUSE.add(g3);
                }
                {
                    TokenGroupMatch g3 = new TokenGroupMatch();
                    g3.append(new TokenStructureMatch("STORE_SLOT")
                            .add(new TokenItemMatch(null, "score").setName("SUBCOMMAND_NODE"))
                            .add(new TokenItemMatch(null, "score").setName("SUBCOMMAND_NODE")));
                    g3.append(ENTITY);
                    g3.append(ANY_STRING);
                    STORE_CLAUSE.add(g3);
                }
                g2.append(STORE_CLAUSE);

                EXECUTE_SUBCOMMAND.add(g2);
            }

            TokenGroupMatch cmd = new TokenGroupMatch().setName("EXECUTE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "execute")).setName("COMMAND_HEADER"));

            cmd.append(new TokenListMatch(EXECUTE_SUBCOMMAND, true));
            cmd.append(new TokenGroupMatch().setName("EXECUTE_SUBCOMMAND").append(new TokenItemMatch(null, "run").setName("SUBCOMMAND_HEADER")).append(COMMAND));

            COMMAND.add(cmd);
        }

        //experience command
        {
            TokenStructureMatch EXPERIENCE_UNIT = new TokenStructureMatch("EXPERIENCE_UNIT");
            EXPERIENCE_UNIT.add(new TokenItemMatch(null, "points"));
            EXPERIENCE_UNIT.add(new TokenItemMatch(null, "levels"));

            { //add/set
                TokenGroupMatch cmd = new TokenGroupMatch().setName("EXPERIENCE_COMMAND");
                cmd.append(new TokenStructureMatch("COMMAND_HEADER").add(new TokenItemMatch(null, "experience")).add(new TokenItemMatch(null, "xp")));

                cmd.append(new TokenStructureMatch("EXPERIENCE_ACTION").add(new TokenItemMatch(null, "set").setName("COMMAND_NODE")).add(new TokenItemMatch(null, "add").setName("COMMAND_NODE")));

                cmd.append(ENTITY);

                cmd.append(INTEGER_NUMBER);

                cmd.append(new TokenGroupMatch(true).append(EXPERIENCE_UNIT));

                COMMAND.add(cmd);
            }

            { //query
                TokenGroupMatch cmd = new TokenGroupMatch().setName("EXPERIENCE_COMMAND");
                cmd.append(new TokenStructureMatch("COMMAND_HEADER").add(new TokenItemMatch(null, "experience")).add(new TokenItemMatch(null, "xp")));

                cmd.append(new TokenStructureMatch("EXPERIENCE_ACTION").add(new TokenItemMatch(null, "query").setName("COMMAND_NODE")).add(new TokenItemMatch(null, "query").setName("COMMAND_NODE")));

                cmd.append(ENTITY);

                cmd.append(new TokenGroupMatch().append(EXPERIENCE_UNIT));

                COMMAND.add(cmd);
            }
        }

        //fill command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("FILL_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "fill")).setName("COMMAND_HEADER"));

            cmd.append(COORDINATE_SET);
            cmd.append(COORDINATE_SET);

            cmd.append(BLOCK);

            TokenStructureMatch following = new TokenStructureMatch("FILL_BRANCH");
            TokenStructureMatch FILL_MODE = new TokenStructureMatch("FILL_MODE");
            FILL_MODE.add(new TokenItemMatch(null, "keep").setName("COMMAND_NODE"));
            FILL_MODE.add(new TokenItemMatch(null, "replace").setName("COMMAND_NODE"));
            FILL_MODE.add(new TokenItemMatch(null, "outline").setName("COMMAND_NODE"));
            FILL_MODE.add(new TokenItemMatch(null, "hollow").setName("COMMAND_NODE"));
            FILL_MODE.add(new TokenItemMatch(null, "destroy").setName("COMMAND_NODE"));

            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(FILL_MODE);
                following.add(g);
            }
            {
                TokenGroupMatch g = new TokenGroupMatch();
                g.append(new TokenGroupMatch().setName("FILL_MODE").append(new TokenItemMatch(null, "replace").setName("COMMAND_NODE")));
                g.append(BLOCK_TAGGED);
                following.add(g);
            }

            cmd.append(new TokenGroupMatch(true).append(following));

            COMMAND.add(cmd);
        }

        //function command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("FUNCTION_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "function")).setName("COMMAND_HEADER"));

            cmd.append(RESOURCE_LOCATION);

            COMMAND.add(cmd);
        }

        //gamemode command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("GAMEMODE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "gamemode")).setName("COMMAND_HEADER"));

            cmd.append(GAMEMODE);
            cmd.append(new TokenGroupMatch(true).append(ENTITY));

            COMMAND.add(cmd);
        }

        //give command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("GIVE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "give")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);
            cmd.append(ITEM);
            cmd.append(new TokenGroupMatch(true).append(INTEGER_NUMBER));

            COMMAND.add(cmd);
        }

        //help command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("HELP_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "help")).setName("COMMAND_HEADER"));

            COMMAND.add(cmd);
        }

        //kill command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("KILL_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "kill")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY);

            COMMAND.add(cmd);
        }

        //list command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("LIST_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "list")).setName("COMMAND_HEADER"));

            COMMAND.add(cmd);
        }

        //locate command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("LOCATE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "locate")).setName("COMMAND_HEADER"));

            cmd.append(STRUCTURE);

            COMMAND.add(cmd);
        }

        //particle command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("PARTICLE_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "particle")).setName("COMMAND_HEADER"));

            cmd.append(PARTICLE);

            {
                TokenGroupMatch g = new TokenGroupMatch(true);
                g.append(COORDINATE_SET);

                {
                    TokenGroupMatch g2 = new TokenGroupMatch(true);
                    g2.append(REAL_NUMBER); //delta-x
                    g2.append(REAL_NUMBER); //delta-y
                    g2.append(REAL_NUMBER); //delta-z

                    g2.append(REAL_NUMBER); //speed

                    g2.append(INTEGER_NUMBER); //count

                    {
                        TokenGroupMatch g3 = new TokenGroupMatch(true);

                        g3.append(new TokenStructureMatch("PARTICLE_MODE").add(new TokenItemMatch(null, "normal").setName("COMMAND_NODE")).add(new TokenItemMatch(null, "force").setName("COMMAND_NODE")));

                        g3.append(new TokenGroupMatch(true).append(ENTITY));

                        g2.append(g3);
                    }

                    g.append(g2);
                }

                cmd.append(g);
            }

            COMMAND.add(cmd);
        }

        //summon command
        {
            TokenGroupMatch cmd = new TokenGroupMatch().setName("SUMMON_COMMAND");
            cmd.append(new TokenGroupMatch().append(new TokenItemMatch(null, "summon")).setName("COMMAND_HEADER"));

            cmd.append(ENTITY_ID);

            cmd.append(new TokenGroupMatch(true).append(COORDINATE_SET).append(new TokenGroupMatch(true).append(NBT_COMPOUND)));

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
