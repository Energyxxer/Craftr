package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.codegen.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.codegen.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.codegen.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

public class Expression extends Value implements FunctionWriter {
    protected boolean silent = false;

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
    public boolean isExplicit() {
        return false;
    }

    @Override
    public Value unwrap(MCFunction function) {
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
    protected Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + " " + op.getSymbol() + " " + b + ")";
    }

    @Override
    public Value writeToFunction(MCFunction function) {
        return a.runOperation(this.op, b, pattern, function, false, silent);
    }

    @Override
    public UnresolvedObjectiveReference getReference() {
        throw new IllegalStateException("Dude, you shouldn't access an expression reference directly, first unwrap.");
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public Value clone(MCFunction function) {
        throw new IllegalStateException("Dude, you shouldn't clone an expression directly, first unwrap.");
    }
}
