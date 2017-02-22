package com.energyxxer.craftr.compile.parsing.classes.evaluation;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenStructure;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrBooleanValue;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrFloatValue;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrIntegerValue;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrNullValue;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrStringValue;
import com.energyxxer.craftr.global.Console;

/**
 * Created by User on 2/18/2017.
 */
public class LiteralEvaluators {
    public static final void registerEvaluators() {
        //Boolean
        Evaluator.addEvaluator("BOOLEAN", p -> {
            if(p.getType().equals("ITEM")) {
                TokenItem item = (TokenItem) p;
                switch(item.getContents().value) {
                    case "true": {
                        return new CraftrBooleanValue(true);
                    }
                    case "false": {
                        return new CraftrBooleanValue(false);
                    }
                    default: {
                        Console.err.println("[ERROR] Boolean token with value \"" + item.getContents().value + "\"");
                        return null;
                    }
                }
            } else {
                Console.err.println("[ERROR] Boolean pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });

        //Float
        Evaluator.addEvaluator("NUMBER", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                float value;

                if(str.matches("\\d+(\\.\\d+)?(f)?")) {
                    if(str.endsWith("f")) {
                        str = str.substring(0,str.length()-1);
                    }
                    value = Float.valueOf(str);
                    return new CraftrFloatValue(value);
                }

                return null;
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });

        //Integer
        Evaluator.addEvaluator("NUMBER", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                int value;

                if(str.matches("\\d+")) {
                    value = Integer.valueOf(str);
                    return new CraftrIntegerValue(value);
                }

                return null;
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });

        //Null

        Evaluator.addEvaluator("NULL", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String str = item.getContents().value;
                return (str.equals("null") ? new CraftrNullValue() : null);
            } else {
                Console.err.println("[ERROR] Number pattern with type " + p.getType() + ": " + p);
                return null;
            }
        });

        //String
        Evaluator.addEvaluator("STRING", p -> {
            if(p instanceof TokenItem) {
                TokenItem item = (TokenItem) p;
                String stringValue = item.getContents().value;
                StringBuilder sb = new StringBuilder();
                boolean escaped = false;
                for(int i = 1; i < stringValue.length()-1; i++) {
                    char c = stringValue.charAt(i);
                    if(escaped) {
                        if(c == 'n') {
                            sb.append('\n');
                        } else {
                            sb.append(c);
                        }
                        escaped = false;
                    } else if(c == '\\') {
                        escaped = true;
                    } else {
                        sb.append(c);
                    }
                }
                return new CraftrStringValue(sb.toString());
            } else {
                Console.warn.println("String pattern of non-item type: " + p);
                return null;
            }
        });

        //Labeled Value Structure
        Evaluator.addEvaluator("VALUE", p -> {
            if(p instanceof TokenStructure) {
                return Evaluator.eval(((TokenStructure) p).getContents());
            }
            return null;
        });

        //Expressions
        Evaluator.addEvaluator("EXPRESSION", p -> {
            if(p instanceof TokenStructure) {

            }
            return null;
        });
    }
}
