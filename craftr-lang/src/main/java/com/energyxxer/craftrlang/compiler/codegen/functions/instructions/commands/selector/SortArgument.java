package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector;

public class SortArgument implements SelectorArgument {
    public enum SortType {
        NEAREST, FARTHEST, RANDOM, ARBITRARY
    }

    private SortType type;

    public SortArgument(SortType type) {
        this.type = type;
    }

    @Override
    public String getArgumentString() {
        return "sort=" + type.toString().toLowerCase();
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }
}
