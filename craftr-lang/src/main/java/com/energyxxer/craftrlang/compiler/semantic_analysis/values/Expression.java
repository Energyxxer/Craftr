package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

public class Expression extends Value {

    private Value a;
    private Operator op;
    private Value b;

    private TokenPattern<?> pattern;

    public Expression(Value a, Operator op, Value b, TokenPattern<?> pattern, Context context) {
        super(context);
        this.a = a;
        this.op = op;
        this.b = b;

        this.pattern = pattern;
    }

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodManager getMethodManager() {
        return null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + " " + op.getSymbol() + " " + b + ")";
    }

    public Value simplify() {

        if(a instanceof Expression) a = ((Expression) a).simplify();
        if(b instanceof Expression) b = ((Expression) b).simplify();

        if(a.explicit && b.explicit) {
            return a.runOperation(this.op, b, pattern);
        } return this;
    }
}
