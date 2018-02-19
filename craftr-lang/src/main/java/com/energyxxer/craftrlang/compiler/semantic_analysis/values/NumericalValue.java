package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public abstract class NumericalValue extends Value {
    public NumericalValue(SemanticContext semanticContext) {
        super(semanticContext);
    }

    public NumericalValue(LocalScore reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    public abstract int getWeight();

    public abstract NumericalValue coerce(NumericalValue value);

    public abstract Number getRawValue();
}
