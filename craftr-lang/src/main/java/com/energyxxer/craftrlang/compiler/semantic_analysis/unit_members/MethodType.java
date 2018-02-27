package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

/**
 * Created by User on 5/16/2017.
 */
public enum MethodType {
    METHOD("met"), CONSTRUCTOR("con"), EVENT("evt"), OPERATOR_OVERLOAD("op");

    private String prefix;

    MethodType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
