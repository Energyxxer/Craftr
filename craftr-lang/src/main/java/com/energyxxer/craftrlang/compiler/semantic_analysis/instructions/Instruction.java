package com.energyxxer.craftrlang.compiler.semantic_analysis.instructions;

public class Instruction {
    private String testMessage;

    public Instruction(String testMessage) {
        this.testMessage = testMessage;
    }

    @Override
    public String toString() {
        return testMessage;
    }
}
