package com.energyxxer.craftrlang.compiler.code_generation.functions;

import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.Comment;
import com.energyxxer.craftrlang.compiler.code_generation.functions.commands.Instruction;

import java.util.ArrayList;

public class MCFunction {
    private String name;

    public ArrayList<Instruction> instructions = new ArrayList<>();

    public MCFunction(String name) {
        this.name = name;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addComment(String comment) {
        instructions.add(new Comment(comment));
    }

    public void addFunction(MCFunction other) {
        //contentBuilder.append(other.contentBuilder);
    }

    public String getName() {
        return name;
    }

    public static String assemble(Instruction instruction) {
        StringBuilder sb = new StringBuilder();
        if(instruction.getPreInstruction() != null) {
            sb.append(assemble(instruction.getPreInstruction()));
        }
        if(instruction.getLines() != null) {
            for(String line : instruction.getLines()) {
                sb.append(line);
                sb.append('\n');
            }
        }
        if(instruction.getPostInstruction() != null) {
            sb.append(assemble(instruction.getPostInstruction()));
        }
        return sb.toString();
    }

    public String build() {
        StringBuilder sb = new StringBuilder("# >> " + name + "\n");
        for(Instruction i : instructions) {
            sb.append(assemble(i));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\n >> " + name + "\n" + "[" + instructions.size() + " top-level instructions]";
    }
}
