package com.energyxxer.cbe.compile.parsing.classes.values;

import com.energyxxer.cbe.util.Range;

/**
 * Created by User on 12/20/2016.
 */
public abstract class CBENumericValue extends CBEValue {
    public Range range = null;

    public abstract int getWeight();
    public abstract float getRawValue();
}
