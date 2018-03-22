package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;

public abstract class NumericValue extends Value {
    public NumericValue(SemanticContext semanticContext) {
        super(semanticContext);
    }

    public NumericValue(DataReference reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    public abstract int getWeight();

    public abstract NumericValue coerce(NumericValue value);

    public abstract Number getRawValue();
}
