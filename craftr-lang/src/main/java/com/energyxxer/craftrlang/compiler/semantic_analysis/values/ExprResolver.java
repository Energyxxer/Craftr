package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodCall;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

import java.util.ArrayList;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public final class ExprResolver {
    public static Value analyzeValue(TokenPattern<?> pattern, Context context, DataHolder dataHolder, MCFunction function) {
        TraversableStructure s = analyzeStructure(pattern, context, dataHolder, function);
        if(s != null) {
            if(s instanceof Variable) return ((Variable) s).getValue();
            if(s instanceof Value) return (Value) s;
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Value expected", pattern.getFormattedPath()));
        }
        return null;
    }

    public static Value analyzeValueOrReference(TokenPattern<?> pattern, Context context, DataHolder dataHolder, MCFunction function) {
        TraversableStructure s = analyzeStructure(pattern, context, dataHolder, function);
        if(s != null) {
            if(s instanceof Variable) return (Variable) s;
            if(s instanceof Value) return (Value) s;
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Value expected", pattern.getFormattedPath()));
        }
        return null;
    }

    public static TraversableStructure analyzeStructure(TokenPattern<?> pattern, Context context, DataHolder dataHolder, MCFunction function) {
        try{
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
                return analyzeStructure(((TokenStructure) pattern).getContents(), context, dataHolder, function);
            } case "EXPRESSION": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), context, dataHolder, function);
            } case "OPERATION": {
                return analyzeStructure(((TokenGroup) pattern).getContents()[0], context, dataHolder, function);
            } case "OPERATION_LIST": {
                TokenList list = (TokenList) pattern;

                if(list.size() == 1) {
                    return analyzeStructure(list.getContents()[0], context, dataHolder, function);
                } else {

                    TokenPattern<?>[] contents = list.getContents();

                    ArrayList<Value> flatValues = new ArrayList<>();
                    ArrayList<Operator> flatOperators = new ArrayList<>();

                    for(int i = 0; i < contents.length; i++) {
                        if((i & 1) == 0) {
                            //Operand
                            flatValues.add(analyzeValueOrReference(contents[i], context, dataHolder, function));
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
                //Methods can only return values, not other traversable objects
                return analyzeValue(((TokenStructure) pattern).getContents(), context, dataHolder, function);
            } case "METHOD_CALL_INNER": {
                if(dataHolder == null) dataHolder = (context.isStatic()) ? context.getUnit() : context.getUnit().getGenericInstance();

                MethodCall call = new MethodCall(pattern, dataHolder, function, context); //REPLACE CONTEXT METHOD LOG BY A MORE SOPHISTICATED SYSTEM AKA AN ACTUAL OBJECT REFERENCE
                call.writeToFunction(function);

                return call;
            } case "SINGLE_IDENTIFIER": {
                if(dataHolder == null) dataHolder = (context.isStatic()) ? context.getUnit() : context.getUnit().getGenericInstance();
                //  CURRENTLY IGNORES LOCAL VARIABLES!!

                if(dataHolder.getSubSymbolTable() != null) {
                    Symbol symbol = dataHolder.getSubSymbolTable().getSymbol(((TokenItem) pattern).getContents(), context);
                    if(symbol != null) {
                        if(symbol instanceof TraversableStructure) return (TraversableStructure) symbol;
                    } return null;
                }
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                return null;
            } case "POINTER": {
                if(dataHolder == null) dataHolder = (context.isStatic()) ? context.getUnit() : context.getUnit().getGenericInstance();
                //  CURRENTLY IGNORES LOCAL VARIABLES!!

                if(dataHolder.getSubSymbolTable() != null) {
                    TraversableStructure s = analyzeStructure(pattern.find("VALUE"),context, dataHolder, function);

                    if(s != null) {
                        if(s instanceof DataHolder) return analyzeStructure(pattern.find("NESTED_POINTER"), context, (DataHolder) s, function);
                        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                    }
                    return null;
                }
                context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                return null;
            } case "NESTED_POINTER": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), context, dataHolder, function);
            } case "NESTED_POINTER_INNER": {
                //Promise that dataHolder won't be null in this case;
                TraversableStructure s = analyzeStructure(pattern.find("POINTER_SEGMENT"), context, dataHolder, function);

                TokenPattern<?> next = pattern.find("POINTER_NEXT");
                if(next == null) return s;
                else if(s != null) {
                    if(s instanceof DataHolder) return analyzeStructure(pattern.find("POINTER_NEXT"), context, (DataHolder) s, function);
                    context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                }
                return null;
            } case "POINTER_SEGMENT": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), context, dataHolder, function);
            } case "POINTER_NEXT": {
                return analyzeStructure(pattern.find("NESTED_POINTER"), context, dataHolder, function);
            }
        }
        } catch(NullPointerException npe) {
            System.out.println("pattern = " + pattern);
            throw npe;
        }

        context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Unresolved Expressions", NoticeType.INFO, "Non-registered exit: " + pattern.getName(), pattern.getFormattedPath()));
        return null;
    }
}
