package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.*;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.CompoundSymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodCall;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public final class ExprResolver {

    public static Value analyzeValue(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, Function function) {
        return analyzeValue(pattern, semanticContext, dataHolder, function, false);
    }

    public static Value analyzeValue(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, Function function, boolean silent) {
        TraversableStructure s = analyzeStructure(pattern, semanticContext, dataHolder, function, silent);
        if(s != null) {
            if(s instanceof Variable) return ((Variable) s).getValue();
            if(s instanceof Value) return (Value) s;
            if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Value expected", pattern.getFormattedPath()));
        }
        return null;
    }

    public static Value analyzeValueOrReference(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, Function function) {
        return analyzeValueOrReference(pattern, semanticContext, dataHolder, function, false);
    }

    public static Value analyzeValueOrReference(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, Function function, boolean silent) {
        TraversableStructure s = analyzeStructure(pattern, semanticContext, dataHolder, function, silent);
        if(s != null) {
            if(s instanceof Variable) return (Variable) s;
            if(s instanceof Value) return (Value) s;
            if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Value expected", pattern.getFormattedPath()));
        }
        return null;
    }

    //public static TraversableStructure analyzeStructure(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, MCFunction function) {
    //    return analyzeStructure(pattern, semanticContext, dataHolder, function, false);
    //}

    private static TraversableStructure analyzeStructure(TokenPattern<?> pattern, SemanticContext semanticContext, DataHolder dataHolder, Function function, boolean silent) {
        try{
        switch(pattern.getName()) {
            case "NUMBER": {
                String raw = pattern.flatten(false);
                if(Character.isLetter(raw.charAt(raw.length()-1))) {
                    raw = raw.substring(0, raw.length()-1);
                }
                if(raw.matches("\\d+\\.\\d+")) { //IS A FLOAT
                    try {
                        return new FloatValue(Float.parseFloat(raw), semanticContext); //TODO: CONVERT THIS TO DOUBLE
                    } catch(NumberFormatException x) {
                        if(!silent) System.err.println("[Something went slightly wrong] Number structure '" + raw + "'is not a valid number. (?)");
                        return null;
                    }
                } else { //IS AN INTEGER
                    try {
                        return new IntegerValue(Integer.parseInt(raw), semanticContext);
                    } catch(NumberFormatException x) {
                        if(!silent) System.err.println("[Something went slightly wrong] Number structure '" + raw + "'is not a valid number. (?)");
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
                        if(!silent) System.err.println("[Something went horribly wrong] Boolean value '" + raw + "' (?)");
                        return null;
                    }
                }
                return new BooleanValue(value, semanticContext);
            } case "VALUE": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), semanticContext, dataHolder, function, silent);
            } case "EXPRESSION": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), semanticContext, dataHolder, function, silent);
            } case "OPERATION": {
                return analyzeStructure(((TokenGroup) pattern).getContents()[0], semanticContext, dataHolder, function, silent);
            } case "OPERATION_LIST": {
                TokenList list = (TokenList) pattern;

                if(list.size() == 1) {
                    return analyzeStructure(list.getContents()[0], semanticContext, dataHolder, function, silent);
                } else {

                    TokenPattern<?>[] contents = list.getContents();

                    ArrayList<Value> flatValues = new ArrayList<>();
                    ArrayList<Operator> flatOperators = new ArrayList<>();

                    for(int i = 0; i < contents.length; i++) {
                        if((i & 1) == 0) {
                            //Operand
                            Value value = analyzeValueOrReference(contents[i], semanticContext, null, function, silent);
                            if(value == null) return null;
                            flatValues.add(value);
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
                        Expression expr = new Expression(a, topOperator, b, pattern, semanticContext);
                        expr.setSilent(silent);
                        flatValues.add(index, expr);
                        flatOperators.remove(index);
                    }

                    Expression expr = (Expression) flatValues.get(0);

                    try {
                        return expr.simplify();
                    } catch(NullPointerException npe) {
                        if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("NullPointerException", NoticeType.INFO, list.toString()));
                        npe.printStackTrace();
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
                                if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal escape character in a string literal", pattern.getFormattedPath()));
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
                return new StringValue(sb.toString(), semanticContext);
            } case "METHOD_CALL": {
                //Methods can only return values, not just any traversable object
                return analyzeValue(((TokenStructure) pattern).getContents(), semanticContext, dataHolder, function, silent);
            } case "METHOD_CALL_INNER": {
                if(dataHolder == null) dataHolder = semanticContext.getInstance();
                if(dataHolder == null) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "WHOA WHOA THERE, THERE'S NO DATA HOLDER??? CRUD", pattern.getFormattedPath()));
                    return null;
                }
                return new MethodCall(pattern, dataHolder, function, semanticContext).evaluate();
            } case "INSTANTIATION": {
                TokenPattern<?> methodCall = ((TokenGroup) pattern).getContents()[1];

                Token unitName = ((TokenItem) methodCall.find("METHOD_CALL_NAME")).getContents();

                Symbol sym = semanticContext.getDeclaringFile().getReferenceTable().getSymbol(Collections.singletonList(unitName), semanticContext, silent);

                if(sym == null) return null;

                if(!(sym instanceof Unit)) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Expected unit name", unitName.getFormattedPath()));
                    return null;
                } else dataHolder = (Unit) sym;

                return analyzeValue(methodCall, semanticContext, dataHolder, function, silent);
            } case "SINGLE_IDENTIFIER": {
                if(dataHolder == null) dataHolder = semanticContext.getDataHolder();
                if(dataHolder == null) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "WHOA WHOA THERE, THERE'S NO DATA HOLDER??? CRUD", pattern.getFormattedPath()));
                    return null;
                }
                if(dataHolder.getSubSymbolTable() != null) {
                    final DataHolder originalDataHolder = dataHolder;
                    dataHolder = new DataHolder() {

                        private CompoundSymbolTable table = new CompoundSymbolTable(originalDataHolder.getSubSymbolTable(), semanticContext.getDeclaringFile().getReferenceTable());

                        @Override
                        public SymbolTable getSubSymbolTable() {
                            return table;
                        }

                        @Override
                        public MethodLog getMethodLog() {
                            return originalDataHolder.getMethodLog();
                        }
                    };
                }

                if(dataHolder.getSubSymbolTable() != null) {
                    Symbol symbol = dataHolder.getSubSymbolTable().getSymbol(((TokenItem) pattern).getContents(), semanticContext, silent);
                    if(symbol != null) {
                        if(symbol instanceof TraversableStructure) return (TraversableStructure) symbol;
                    } return null;
                }
                if(!silent) {
                    semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                }
                return null;
            } case "POINTER": {
                if(dataHolder == null) dataHolder = semanticContext.getDataHolder();
                if(dataHolder == null) {
                    if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.WARNING, "WHOA WHOA THERE, THERE'S NO DATA HOLDER??? CRUD", pattern.getFormattedPath()));
                    return null;
                }
                if(dataHolder.getSubSymbolTable() != null) {
                    final DataHolder originalDataHolder = dataHolder;
                    dataHolder = new DataHolder() {

                        private CompoundSymbolTable table = new CompoundSymbolTable(originalDataHolder.getSubSymbolTable(), semanticContext.getDeclaringFile().getReferenceTable());

                        @Override
                        public SymbolTable getSubSymbolTable() {
                            return table;
                        }

                        @Override
                        public MethodLog getMethodLog() {
                            return originalDataHolder.getMethodLog();
                        }
                    };
                }

                if(dataHolder.getSubSymbolTable() != null) {
                    TraversableStructure s = analyzeStructure(pattern.find("VALUE"), semanticContext, dataHolder, function, silent);

                    if(s != null) {
                        if(s instanceof DataHolder) return analyzeStructure(pattern.find("NESTED_POINTER"), semanticContext, (DataHolder) s, function, silent);
                        if(!silent) {
                            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                        }
                    }
                    return null;
                }
                if(!silent) {
                    semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                }
                return null;
            } case "NESTED_POINTER": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), semanticContext, dataHolder, function, silent);
            } case "NESTED_POINTER_INNER": {
                //Promise that dataHolder won't be null in this case;
                TraversableStructure s = analyzeStructure(pattern.find("POINTER_SEGMENT"), semanticContext, dataHolder, function, silent);

                TokenPattern<?> next = pattern.find("POINTER_NEXT");
                if(next == null) return s;
                else if(s != null) {
                    if(s instanceof DataHolder) return analyzeStructure(pattern.find("POINTER_NEXT"), semanticContext, (DataHolder) s, function, silent);
                    if(!silent) {
                        semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve symbol from an undefined data holder", pattern.getFormattedPath()));
                    }
                }
                return null;
            } case "POINTER_SEGMENT": {
                return analyzeStructure(((TokenStructure) pattern).getContents(), semanticContext, dataHolder, function, silent);
            } case "POINTER_NEXT": {
                return analyzeStructure(pattern.find("NESTED_POINTER"), semanticContext, dataHolder, function, silent);
            } case "NULL" : {
                return new Null(semanticContext);
            }
        }
        } catch(NullPointerException npe) {
            if(!silent) System.out.println("pattern = " + pattern);
            throw npe;
        }

        if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Unresolved Expressions", NoticeType.INFO, "Non-registered exit: " + pattern.getName(), pattern.getFormattedPath()));
        return null;
    }
}
