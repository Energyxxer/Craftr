package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members.MethodCall;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.OperandType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

public class Expression extends ValueWrapper {
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

    private boolean usesVariable(Variable variable) {
        return a == variable || b == variable || a instanceof Expression && ((Expression) a).usesVariable(variable) || b instanceof Expression && ((Expression) b).usesVariable(variable) || a instanceof MethodCall || b instanceof MethodCall;
    }

    public Value unwrap(FunctionSection section) {
        return unwrap(section, null);
    }

    public Value unwrap(FunctionSection section, ScoreReference resultReference) {

        if(a instanceof Expression) a = ((Expression) a).unwrap(section, null);
        if(a == null) return null;
        if(b instanceof Expression) {
            if(op == Operator.ASSIGN && a instanceof Variable && a.getReference() instanceof ScoreReference && !((Expression) b).usesVariable((Variable) a)) {
                b = ((Expression) b).unwrap(section, (ScoreReference) a.getReference());
            } else {
                b = ((Expression) b).unwrap(section, null);
            }
        }
        if(b == null) return null;

        if(op.getLeftOperandType() == OperandType.VALUE && a instanceof Variable) {
            a = ((Variable) a).unwrap();
        } else if(op.getLeftOperandType() == OperandType.VARIABLE && !(a instanceof Variable)) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid left-hand side in " + op.getSymbol() + " operation", pattern));
            return null;
        } else if(a instanceof ValueWrapper && op.getLeftOperandType() != OperandType.VARIABLE) {
            a = ((ValueWrapper) a).unwrap(section);
        }

        if(op.getRightOperandType() == OperandType.VALUE && b instanceof Variable) {
            b = ((Variable) b).unwrap();
        } else if(op.getRightOperandType() == OperandType.VARIABLE && !(b instanceof Variable)) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Invalid right-hand side in " + op.getSymbol() + " operation", pattern));
            return null;
        } else if(b instanceof ValueWrapper && op.getRightOperandType() != OperandType.VARIABLE) {
            b = ((ValueWrapper) b).unwrap(section);
        }

        if(a == null) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Left-hand side value may not have been initialized", pattern));
            return null;
        }

        if(b == null) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Right-hand side value may not have been initialized", pattern));
            return null;
        }

        Value returnValue = a.runOperation(this.op, b, pattern, section, semanticContext, resultReference, this.silent);
        if(returnValue == null) {
            semanticContext.getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator " + op.getSymbol() + " is not defined for data types " + a.getDataType() + ", " + b.getDataType(), pattern));
        } else if(resultReference != null) {
            //returnValue.getReference().toScore(function, resultReference.getScore(), semanticContext);
        }
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
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        return null;
    }

    @Override
    public String toString() {
        return "(" + a + " " + op.getSymbol() + " " + b + ")";
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
