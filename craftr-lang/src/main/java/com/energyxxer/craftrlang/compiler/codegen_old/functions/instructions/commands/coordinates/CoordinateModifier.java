package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.coordinates;

public interface CoordinateModifier {
    String getSubCommand();

    boolean isIdempotent();
    boolean isSignificant();
    boolean isAbsolute();
}
