package com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit;

import com.energyxxer.commodore.commands.scoreboard.ScoreComparison;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;

public interface ExplicitValue extends DataReference {

    Number asNumber();

    static boolean compare(Number a, ScoreComparison op, Number b) {
        if(a == null) throw new NullPointerException("a is null");
        if(b == null) throw new NullPointerException("b is null");
        if(op == ScoreComparison.GREATER_THAN || op == ScoreComparison.GREATER_THAN_EQUAL) {
            Number oldA = a;
            a = b;
            b = oldA;
            op = op.getReverse();
        }

        switch(op) {
            case LESS_THAN: return a.doubleValue() < b.doubleValue();
            case LESS_THAN_EQUAL: return a.doubleValue() <= b.doubleValue();
            case EQUAL: return a.doubleValue() == b.doubleValue();
        }
        throw new IllegalArgumentException("Illegal score comparison '" + op + "'");
    }

    @Override
    default BooleanResolution compare(Function function, ScoreComparison op, DataReference other, SemanticContext semanticContext) {
        if(other instanceof ExplicitValue) {
            return new BooleanResolution(compare(asNumber(), op, ((ExplicitValue) other).asNumber()));
        } else {
            return other.compare(function, op.getReverse(), other, semanticContext); //We don't allow yoda speak here
        }
    }
}
