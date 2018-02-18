package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector;

public class TypeArgument implements SelectorArgument {
    private String type;
    private boolean negated;

    public TypeArgument(String type) {
        if(type.startsWith("!")) {
            this.type = type.substring(1);
            this.negated = true;
        } else {
            this.type = type;
            this.negated = false;
        }
    }

    public String getType() {
        return type;
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public String getArgumentString() {
        return "type=" + type;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public String toString() {
        return getArgumentString();
    }
}
