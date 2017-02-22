package com.energyxxer.craftr.compile.parsing.classes.evaluation;

import com.energyxxer.craftr.compile.parsing.classes.values.CraftrValue;

/**
 * Created by User on 2/18/2017.
 */
public abstract class ExpressionTree extends CraftrValue {
    private CraftrValue operand1;
    private Operator operator;
    private CraftrValue operand2;


}
