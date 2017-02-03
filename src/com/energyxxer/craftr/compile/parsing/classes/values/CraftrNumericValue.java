package com.energyxxer.craftr.compile.parsing.classes.values;

import com.energyxxer.craftr.util.Range;

/**
 * Created by User on 12/20/2016.
 */
public abstract class CraftrNumericValue extends CraftrValue {
    public Range range = null;

    public abstract int getWeight();
    public abstract float getRawValue();
}
