package com.energyxxer.cbe.compile.parsing.classes.evaluation;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.values.CBEValue;

/**
 * Created by User on 1/30/2017.
 */
public interface PatternParser {
    CBEValue eval(TokenPattern<?> p);
}
