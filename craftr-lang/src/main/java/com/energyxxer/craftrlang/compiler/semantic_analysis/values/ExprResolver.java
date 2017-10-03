package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.Method;

import java.util.ArrayList;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public final class ExprResolver {

    public static Value analyzeValue(TokenPattern<?> pattern, Context context, MCFunction function) {
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
                return analyzeValue(((TokenStructure) pattern).getContents(), context, function);
            } case "EXPRESSION": {
                return analyzeValue(((TokenStructure) pattern).getContents(), context, function);
            } case "OPERATION": {
                return analyzeValue(((TokenGroup) pattern).getContents()[0], context, function);
            } case "OPERATION_LIST": {
                TokenList list = (TokenList) pattern;

                if(list.size() == 1) {
                    return analyzeValue(list.getContents()[0], context, function);
                } else {

                    TokenPattern<?>[] contents = list.getContents();

                    ArrayList<Value> flatValues = new ArrayList<>();
                    ArrayList<Operator> flatOperators = new ArrayList<>();

                    for(int i = 0; i < contents.length; i++) {
                        if((i & 1) == 0) {
                            //Operand
                            flatValues.add(analyzeValue(contents[i], context, function));
                        } else {
                            //Operator
                            flatOperators.add(Operator.getOperatorForSymbol(((TokenItem) contents[i]).getContents().value));
                        }
                    }

                    while(flatOperators.size() >= 1) {
                        int index = -1;
                        Operator topOperator = null;

                        for(int i = 0; i < flatOperators.size(); i++) {
                            Operator op = flatOperators.get(i);
                            if(topOperator == null) {
                                index = i;
                                topOperator = op;
                            } else if(topOperator.getPrecedence() >= op.getPrecedence()) {

                                if(topOperator.getPrecedence() > op.getPrecedence() || topOperator.isRightToLeft()) {
                                    index = i;
                                    topOperator = op;
                                }
                            }
                        }

                        Value a = flatValues.get(index);
                        Value b = flatValues.get(index+1);

                        flatValues.remove(index);
                        flatValues.remove(index);
                        flatValues.add(index, new Expression(a, topOperator, b, pattern, context));
                        flatOperators.remove(index);
                    }

                    Expression expr = (Expression) flatValues.get(0);

                    try {
                        return expr.simplify();
                    } catch(NullPointerException npe) {
                        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("NullPointerException", NoticeType.INFO, list.toString()));
                        return null;
                    }
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
            } case "METHOD_CALL": {
                return analyzeValue(((TokenStructure) pattern).getContents(), context, function);
            } case "METHOD_CALL_INNER": {
                Token methodNameToken = ((TokenItem) pattern.find("METHOD_CALL_NAME")).getContents();

                Symbol ref = context.findSymbol(methodNameToken, true);
                if(ref != null && ref instanceof Method) {
                    ((Method) ref).getCodeBlock().writeToFunction(function);
                    // THIS IS OBVIOUSLY IGNORING ALL ACTUAL PARAMETERS, TODO.
                } else {
                    context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method '" + methodNameToken.value + "'", pattern.getFormattedPath()));
                }
            }
        }
        System.out.println("Non-registered exit: " + pattern.getName());
        return null;
    }
}
