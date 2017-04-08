package com.energyxxer.craftr.compiler.semantic_analysis;

import com.energyxxer.craftr.compiler.parsing.pattern_matching.structures.TokenPattern;

/**
 * Created by User on 2/28/2017.
 */
public abstract class AbstractCraftrComponent {
    public final TokenPattern<?> pattern;

    public AbstractCraftrComponent(TokenPattern<?> pattern) {
        this.pattern = pattern;
    }
}
