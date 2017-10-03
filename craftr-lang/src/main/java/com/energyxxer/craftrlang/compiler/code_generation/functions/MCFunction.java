package com.energyxxer.craftrlang.compiler.code_generation.functions;

public class MCFunction {
    private String name;
    private StringBuilder contentBuilder = new StringBuilder();

    public MCFunction(String name) {
        this.name = name;
    }

    public void addCommand(String command) {
        contentBuilder.append(command);
        contentBuilder.append('\n');
    }

    public void addComment(String comment) {
        contentBuilder.append("# ");
        contentBuilder.append(comment);
        contentBuilder.append('\n');
    }

    public void addFunction(MCFunction other) {
        contentBuilder.append(other.contentBuilder);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "\n >> " + name + "\n" + contentBuilder.toString();
    }
}
