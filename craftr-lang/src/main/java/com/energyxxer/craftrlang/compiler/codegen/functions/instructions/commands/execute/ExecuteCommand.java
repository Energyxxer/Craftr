package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.CompoundInstruction;
import com.energyxxer.craftrlang.compiler.codegen.functions.instructions.Instruction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteCommand implements Instruction {
    private ArrayList<ExecuteSubCommand> subCommands = new ArrayList<>();
    private Instruction commandInstructions;

    private CompoundInstruction preInstruction = new CompoundInstruction();
    private CompoundInstruction postInstruction = new CompoundInstruction();

    public ExecuteCommand(Instruction commands, ExecuteSubCommand... subCommands) {
        this(commands, Arrays.asList(subCommands));
    }

    public ExecuteCommand(Instruction commands, List<ExecuteSubCommand> subCommands) {
        this.commandInstructions = commands;
        this.subCommands.addAll(subCommands);
        for(ExecuteSubCommand sc : this.subCommands) {
            preInstruction.addInstruction(sc.getPreInstruction());
            postInstruction.addInstruction(sc.getPostInstruction());
        }
    }

    @Override
    public Instruction getPreInstruction() {
        return preInstruction;
    }

    @Override
    public @NotNull List<String> getLines() {
        ArrayList<String> commands = new ArrayList<>(commandInstructions.getLines());

        StringBuilder sb = new StringBuilder("execute ");
        for(ExecuteSubCommand subCommand : subCommands) {
            sb.append(subCommand.getSubCommand());
            sb.append(' ');
        }
        sb.append("run ");

        String header = sb.toString();

        for(int i = 0; i < commands.size(); i++) {
            commands.set(i, header + commands.get(i));
        }

        return commands;
    }

    @Override
    public Instruction getPostInstruction() {
        return postInstruction;
    }
}
