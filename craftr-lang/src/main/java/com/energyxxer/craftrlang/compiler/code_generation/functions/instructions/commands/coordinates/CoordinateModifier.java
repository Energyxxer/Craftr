package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.coordinates;

public interface CoordinateModifier {
    String getSubCommand();

    boolean isIdempotent();
    boolean isSignificant();
    boolean isAbsolute();
}
