package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectivePointer;

public class ExecuteStore implements ExecuteSubCommand {
    public enum Action {
        RESULT("result"), SUCCESS("success");

        public final String prefix;

        Action(String prefix) {
            this.prefix = prefix;
        }
    }

    private Action action;
    private ObjectivePointer reference;

    public ExecuteStore(Action action, ObjectivePointer reference) {
        this.action = action;
        this.reference = reference;
    }

    @Override
    public Instruction getPreInstruction() {
        return reference.getEntity().getInstruction();
    }

    @Override
    public String getSubCommand() {
        return "store " + action.prefix + " " + reference.getEntity().toSelector() + " " + reference.getObjectiveName();
    }

    @Override
    public Instruction getPostInstruction() {
        return null;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public ObjectivePointer getReference() {
        return reference;
    }

    public void setReference(ObjectivePointer reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return getSubCommand();
    }
}
