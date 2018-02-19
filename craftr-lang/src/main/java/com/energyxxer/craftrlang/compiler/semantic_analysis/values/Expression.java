package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

public class Expression extends Value {
    protected boolean silent = false;

    private Value a;
    private Operator op;
    private Value b;

    private TokenPattern<?> pattern;

    public Expression(Value a, Operator op, Value b, TokenPattern<?> pattern, SemanticContext semanticContext) {
        super(semanticContext);
        this.a = a;
        this.op = op;
        this.b = b;

        this.pattern = pattern;
    }

    @Override
    public boolean isExplicit() {
        return false;
    }

    @Override
    public Value unwrap(Function function) {
        return this.writeToFunction(function);
    }

    public Value simplify() {

        if(a == null || b == null) return this;

        if(a instanceof Expression) a = ((Expression) a).simplify();
        if(b instanceof Expression) b = ((Expression) b).simplify();

        if(a.isExplicit() && b.isExplicit()) {
            return a.runOperation(this.op, b, pattern, null, false, silent);
        } return this;
    }

    @Override
    public DataType getDataType() {
        return a.getDataType().getReturnType(op, b.getDataType());
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodLog getMethodLog() {
        return null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + " " + op.getSymbol() + " " + b + ")";
    }

    public Value writeToFunction(Function function) {
        return a.runOperation(this.op, b, pattern, function, false, silent);
    }

    @Override
    public LocalScore getReference() {
        throw new IllegalStateException("Dude, you shouldn't access an expression reference directly, first unwrap.");
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public Value clone(Function function) {
        throw new IllegalStateException("Dude, you shouldn't clone an expression directly, first unwrap.");
    }
}
