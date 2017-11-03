package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.code_generation.functions.Score;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.UnresolvedObjectiveReference;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public abstract class Value implements TraversableStructure, Score {
    protected final Context context;
    protected UnresolvedObjectiveReference reference = null;

    /*
    * TODO:  DDDD    OOO
    * TODO:  D   D  O   O
    * TODO:  D   D  O   O
    * TODO:  D   D  O   O
    * TODO:  DDDD    OOO
    *
    * TODO:  L        A    ZZZZZ  Y   Y
    * TODO:  L       A A      Z    Y Y
    * TODO:  L       AAA     Z      Y
    * TODO:  L      A   A   Z       Y
    * TODO:  LLLLL  A   A  ZZZZZ    Y
    *
    * TODO:  IIIII  N   N   SSS  TTTTT  RRRR   U   U   CCCC  TTTTT  IIIII   OOO   N   N
    * TODO:    I    NN  N  S       T    R   R  U   U  C        T      I    O   O  NN  N
    * TODO:    I    N N N   SS     T    RRRR   U   U  C        T      I    O   O  N N N
    * TODO:    I    N  NN     S    T    R  R   U   U  C        T      I    O   O  N  NN
    * TODO:  IIIII  N   N  SSS     T    R   R   UUU    CCCC    T    IIIII   OOO   N   N
    *
    * TODO:  IIIII  N   N   SSS  TTTTT    A    N   N  TTTTT  IIIII    A    TTTTT  IIIII   OOO   N   N
    * TODO:    I    NN  N  S       T     A A   NN  N    T      I     A A     T      I    O   O  NN  N
    * TODO:    I    N N N   SS     T     AAA   N N N    T      I     AAA     T      I    O   O  N N N
    * TODO:    I    N  NN     S    T    A   A  N  NN    T      I    A   A    T      I    O   O  N  NN
    * TODO:  IIIII  N   N  SSS     T    A   A  N   N    T    IIIII  A   A    T    IIIII   OOO   N   N
    *
    * tldr; do lazy instruction instantiation
    * */

    public Value(Context context) {
        this.context = context;
    }

    public Value(UnresolvedObjectiveReference reference, Context context) {
        this.reference = reference;
        this.context = context;
    }

    public boolean isExplicit() {
        return reference == null;
    }

    public void setReference(UnresolvedObjectiveReference reference) {
        this.reference = reference;
    }

    public UnresolvedObjectiveReference getReference() {
        return reference;
    }

    public int getScoreboardValue() {
        return Integer.MIN_VALUE;
    }

    public abstract DataType getDataType();
    public abstract SymbolTable getSubSymbolTable();
    public abstract MethodLog getMethodLog();

    public Value runOperation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {
        Value returnValue = this.operation(operator, pattern, function, fromVariable, silent);
        if(returnValue == null) {
            this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to '" + getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value runOperation(Operator operator, Value value, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent) {

        Value thisUnwrapped = this.unwrap(function);
        Value operandUnwrapped = value.unwrap(function);

        // Unbox variables on the right-hand side
        if(operandUnwrapped != null && operandUnwrapped instanceof Variable) operandUnwrapped = ((Variable) operandUnwrapped).getValue();

        if(thisUnwrapped == null || operandUnwrapped == null) return null;

        Value returnValue = thisUnwrapped.operation(operator, operandUnwrapped, pattern, function, fromVariable, silent);
        if(returnValue == null) {
            if(!silent) this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to types '" + thisUnwrapped.getDataType() + "', '" + operandUnwrapped.getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public Value unwrap(MCFunction function) {
        return this;
    }

    protected abstract Value operation(Operator operator, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent);
    protected abstract Value operation(Operator operator, Value operand, TokenPattern<?> pattern, MCFunction function, boolean fromVariable, boolean silent);

    public abstract Value clone(MCFunction function);

    public boolean isNull() {
        return getDataType() != null && getDataType().isNullType();
    }
}
