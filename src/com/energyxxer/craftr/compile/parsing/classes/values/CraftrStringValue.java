package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenItem;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.classes.evaluation.Evaluator;
import com.energyxxer.craftr.global.Console;
import com.sun.istack.internal.NotNull;

/**
 * Created by User on 2/14/2017.
 */
public class CraftrStringValue extends CraftrValue {

    private String value = null;

    public CraftrStringValue(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String getInternalType() {
        return "String";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public CraftrValue addition(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue subtraction(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue multiplication(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue division(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue modulo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue exponentiate(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue increment() throws IllegalOperandsException {
        return null;
    }

    @Override
    public CraftrValue decrement() throws IllegalOperandsException {
        return null;
    }

    @Override
    public boolean isGreaterThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    @Override
    public boolean isLessThan(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    @Override
    public boolean isGreaterThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    @Override
    public boolean isLessThanOrEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    @Override
    public boolean isEqualTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return false;
    }

    @Override
    public CraftrValue assignTo(@NotNull CraftrValue operand) throws IllegalOperandsException {
        return null;
    }

    public static void init() {
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
    }
}
