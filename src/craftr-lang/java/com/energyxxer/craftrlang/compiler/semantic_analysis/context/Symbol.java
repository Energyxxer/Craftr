package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.Access;

/**
 * Created by User on 3/14/2017.
 */
public interface Symbol {
    String getName();
    Access getAccess();
}
