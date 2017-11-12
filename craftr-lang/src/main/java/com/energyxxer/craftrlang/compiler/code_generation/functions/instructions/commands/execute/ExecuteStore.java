package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.execute;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;
import com.energyxxer.craftrlang.compiler.code_generation.objectives.ResolvedObjectiveReference;

public class ExecuteStore implements ExecuteSubCommand {
    public enum Action {
        RESULT("result"), SUCCESS("success");

        public final String prefix;

        Action(String prefix) {
            this.prefix = prefix;
        }
    }

    private Action action;
    private ResolvedObjectiveReference reference;

    public ExecuteStore(Action action, ResolvedObjectiveReference reference) {
        this.action = action;
        this.reference = reference;
    }

    @Override
    public Instruction getPreInstruction() {
        return reference.getScoreHolderReference().getInstruction();
    }

    @Override
    public String getSubCommand() {
        return "store " + action.prefix + " " + reference.getScoreHolderReference().getSelector() + " " + reference.getObjective().getName();
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

    public ResolvedObjectiveReference getReference() {
        return reference;
    }

    public void setReference(ResolvedObjectiveReference reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return getSubCommand();
    }
}
