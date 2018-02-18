package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector;

public class TagArgument implements SelectorArgument {
    private String tag;
    private boolean negated;

    public TagArgument(String tag) {
        if(tag.startsWith("!")) {
            this.tag = tag.substring(1);
            this.negated = true;
        } else {
            this.tag = tag;
            this.negated = false;
        }
    }

    public String getTag() {
        return tag;
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public String getArgumentString() {
        return "tag=" + (negated ? "!" : "") + tag;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public String toString() {
        return getArgumentString();
    }
}
