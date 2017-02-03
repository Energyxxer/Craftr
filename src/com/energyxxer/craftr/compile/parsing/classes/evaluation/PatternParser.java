package com.energyxxer.craftr.compile.parsing.classes.evaluation;

import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrValue;

/**
 * Created by User on 1/30/2017.
 */
public interface PatternParser {
    CraftrValue eval(TokenPattern<?> p);
}
