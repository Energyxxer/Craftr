package com.energyxxer.craftr.compiler.semantic_analysis.context;

import com.energyxxer.craftr.compiler.semantic_analysis.constants.Access;

/**
 * Created by User on 3/14/2017.
 */
public interface CraftrSymbol {
    String getName();
    Access getAccess();
}
