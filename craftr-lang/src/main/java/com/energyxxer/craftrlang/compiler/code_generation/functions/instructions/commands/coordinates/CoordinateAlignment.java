package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.coordinates;

public class CoordinateAlignment implements CoordinateModifier {
    private boolean x = false;
    private boolean y = false;
    private boolean z = false;

    public CoordinateAlignment(boolean x, boolean y, boolean z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getSubCommand() {
        return "align " + ((x) ? "x" : "") + ((y) ? "y" : "") + ((z) ? "z" : "");
    }

    @Override
    public boolean isIdempotent() {
        return true;
    }

    @Override
    public boolean isSignificant() {
        return x || y || z;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }
}
