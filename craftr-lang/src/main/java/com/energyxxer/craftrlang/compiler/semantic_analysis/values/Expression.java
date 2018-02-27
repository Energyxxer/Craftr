package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperandType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

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
        return a.isExplicit() && b.isExplicit();
    }

    @Override
    public Value unwrap(Function function) {

        if(a instanceof Expression) a = a.unwrap(function);
        if(a == null) return null;
        if(b instanceof Expression) b = b.unwrap(function);
        if(b == null) return null;

        if(op.getLeftOperandType() == OperandType.VALUE && a instanceof Variable) {
            a = ((Variable) a).getValue();
        } else if(op.getLeftOperandType() == OperandType.REFERENCE && !(a instanceof Variable)) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid left-hand side in " + op.getSymbol() + " operation", pattern.getFormattedPath()));
            return null;
        }
        if(op.getRightOperandType() == OperandType.VALUE && b instanceof Variable) {
            b = ((Variable) b).getValue();
        } else if(op.getRightOperandType() == OperandType.REFERENCE && !(b instanceof Variable)) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid right-hand side in " + op.getSymbol() + " operation", pattern.getFormattedPath()));
            return null;
        }

        Value returnValue = a.runOperation(this.op, b, pattern, function, this.silent);
        if(returnValue == null) semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator " + op.getSymbol() + " is not defined for data types " + a.getDataType() + ", " + b.getDataType()));
        return returnValue;
    }

    public Value simplify() {

        /*if(a == null || b == null) return this;

        if(a instanceof Expression) a = ((Expression) a).simplify();
        if(b instanceof Expression) b = ((Expression) b).simplify();

        if(a.isExplicit() && b.isExplicit()) {
            return a.runOperation(this.op, b, pattern, null, silent);
        } return this;*/
        return this;
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
    public Value runOperation(Operator operator, TokenPattern<?> pattern, Function function, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + " " + op.getSymbol() + " " + b + ")";
    }

    public Value writeToFunction(Function function) {
        return a.runOperation(this.op, b, pattern, function, silent);
    }

    @Override
    public DataReference getReference() {
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
