package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public final class ExprParser {

    public static Value parseValue(TokenPattern<?> pattern, Context context) {
        //System.out.println("pattern = " + pattern);

        switch(pattern.getName()) {
            case "NUMBER": {
                String raw = pattern.flatten(false);
                if(Character.isLetter(raw.charAt(raw.length()-1))) {
                    raw = raw.substring(0, raw.length()-1);
                }
                if(raw.matches("\\d+\\.\\d+")) { //IS A FLOAT
                    try {
                        return new FloatValue(Float.parseFloat(raw), context);
                    } catch(NumberFormatException x) {
                        System.err.println("[Something went slightly wrong] Number structure '" + raw + "'is not a valid number. (?)");
                        return null;
                    }
                } else { //IS AN INTEGER
                    try {
                        return new IntegerValue(Integer.parseInt(raw), context);
                    } catch(NumberFormatException x) {
                        System.err.println("[Something went slightly wrong] Number structure '" + raw + "'is not a valid number. (?)");
                        return null;
                    }
                }
            }
            case "BOOLEAN": {
                String raw = pattern.flatten(false);
                boolean value;
                switch(raw) {
                    case "true" : value = true; break;
                    case "false" : value = false; break;
                    default: {
                        System.err.println("[Something went horribly wrong] Boolean value '" + raw + "' (?)");
                        return null;
                    }
                }
                return new BooleanValue(value, context);
            } case "VALUE": {
                return parseValue(((TokenStructure) pattern).getContents(), context);
            } case "EXPRESSION": {
                return parseValue(((TokenStructure) pattern).getContents(), context);
            } case "OPERATION": {
                return parseValue(((TokenGroup) pattern).getContents()[0], context);
            } case "OPERATION_LIST": {
                TokenList list = (TokenList) pattern;
                if(list.size() == 1) {
                    return parseValue(list.getContents()[0], context);
                } else {
                    System.err.println("WHOA WAIT NO, OPERATIONS NOT SUPPORTED YET GEEZ");
                    return null;
                }
            } case "STRING": {
                String raw = pattern.flatten(false);
                StringBuilder sb = new StringBuilder();
                boolean escaped = false;
                for(int i = 1; i < raw.length()-1; i++) {
                    char ch = raw.charAt(i);
                    if(escaped) {
                        escaped = false;
                        switch(ch) {
                            case 'b': {
                                sb.append('\b'); break;
                            }
                            case 'f': {
                                sb.append('\f'); break;
                            }
                            case 'n': {
                                sb.append('\n'); break;
                            }
                            case 'r': {
                                sb.append('\r'); break;
                            }
                            case 't': {
                                sb.append('\t'); break;
                            }
                            case '\\': {
                                sb.append("\\\\"); break;
                            }
                            default: {
                                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal escape character in a string literal", pattern.getFormattedPath()));
                            }
                        }
                    } else {
                        if(ch == '\\') {
                            escaped = true;
                        } else {
                            sb.append(ch);
                        }
                    }
                }
                return new StringValue(sb.toString(), context);
            }
        }
        System.out.println("Non-registered exit: " + pattern.getName());
        return null;
    }
}
