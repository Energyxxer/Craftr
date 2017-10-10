package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public abstract class NumericalValue extends Value {
    public NumericalValue(Context context) {
        super(context);
    }

    public abstract int getWeight();

    public abstract NumericalValue coerce(NumericalValue value);

    public abstract Number getRawValue();
}
