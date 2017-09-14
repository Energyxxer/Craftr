package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodManager;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public abstract class Value {
    protected final Context context;
    protected boolean explicit = true;

    public Value(Context context) {
        this.context = context;
    }

    public final boolean isExplicit() {
        return explicit;
    }

    protected final void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public abstract DataType getDataType();
    public abstract SymbolTable getSubSymbolTable();
    public abstract MethodManager getMethodManager();

    public final Value runOperation(Operator operator, TokenPattern<?> pattern) {
        Value returnValue = this.operation(operator, pattern);
        if(returnValue == null) {
            this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to '" + getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    public final Value runOperation(Operator operator, Value value, TokenPattern<?> pattern) {
        Value returnValue = this.operation(operator, value, pattern);
        if(returnValue == null) {
            this.context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Operator '" + operator.getSymbol() + "' cannot be applied to types '" + getDataType() + "', '" + value.getDataType() + "'", pattern.getFormattedPath()));
        }
        return returnValue;
    }

    protected abstract Value operation(Operator operator, TokenPattern<?> pattern);
    protected abstract Value operation(Operator operator, Value operand, TokenPattern<?> pattern);
}
