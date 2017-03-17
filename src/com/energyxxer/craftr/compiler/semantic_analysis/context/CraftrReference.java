package com.energyxxer.craftr.compiler.semantic_analysis.context;

import com.energyxxer.craftr.compiler.semantic_analysis.constants.Access;

/**
 * Created by User on 3/14/2017.
 */
public class CraftrReference {
    public final String identifier;
    public final Access access;

    public CraftrReference(String identifier) {
        this(identifier, Access.PUBLIC);
    }

    public CraftrReference(String identifier, Access access) {
        this.identifier = identifier;
        this.access = access;
    }
}
