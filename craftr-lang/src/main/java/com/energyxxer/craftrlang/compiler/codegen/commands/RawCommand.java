package com.energyxxer.craftrlang.compiler.codegen.commands;

import com.energyxxer.commodore.commands.Command;
import com.energyxxer.commodore.inspection.CommandResolution;
import com.energyxxer.commodore.inspection.ExecutionContext;
import org.jetbrains.annotations.NotNull;

public class RawCommand implements Command {
    private String command;

    public RawCommand(String command) {
        this.command = command;
    }

    @Override
    public @NotNull CommandResolution resolveCommand(ExecutionContext executionContext) {
        return new CommandResolution(executionContext, command);
    }
}
