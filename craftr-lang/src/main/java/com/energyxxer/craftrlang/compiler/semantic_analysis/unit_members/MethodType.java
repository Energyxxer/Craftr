package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

/**
 * Created by User on 5/16/2017.
 */
public enum MethodType {
    METHOD("met", false), CONSTRUCTOR("con", true), EVENT("evt", false), OPERATOR_OVERLOAD("op", false);

    private String prefix;
    private boolean staticAccess;

    MethodType(String prefix, boolean staticAccess) {
        this.prefix = prefix;
        this.staticAccess = staticAccess;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isStaticAccess() {
        return staticAccess;
    }
}
