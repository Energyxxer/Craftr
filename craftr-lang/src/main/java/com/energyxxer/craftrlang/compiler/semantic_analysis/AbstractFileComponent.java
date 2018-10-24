package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;

/**
 * Created by User on 2/28/2017.
 */
public abstract class AbstractFileComponent {
    public final TokenPattern<?> pattern;

    public AbstractFileComponent(TokenPattern<?> pattern) {
        this.pattern = pattern;
    }
}
