package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.coordinates;

public class CoordinateAtEntity implements CoordinateModifier {

    private String selector = "@s"; //TEMPORARY

    public CoordinateAtEntity(String selector) {
        this.selector = selector;
    }

    @Override
    public String getSubCommand() {
        return "at " + selector;
    }

    @Override
    public boolean isIdempotent() {
        return true;
    }

    @Override
    public boolean isSignificant() {
        return true;
    }

    @Override
    public boolean isAbsolute() {
        return true;
    }
}
