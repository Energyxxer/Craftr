package com.energyxxer.craftrlang.compiler.code_generation.players;

import com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.Instruction;

public class PlayerReference {
    private Player player;
    private String selector;
    private Instruction instruction;

    public PlayerReference(Player player, String selector) {
        this(player, selector, null);
    }

    public PlayerReference(Player player, String selector, Instruction instruction) {
        this.player = player;
        this.selector = selector;
        this.instruction = instruction;
    }

    public Player getPlayer() {
        return player;
    }

    public String getSelector() {
        return selector;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    @Override
    public String toString() {
        return "PlayerReference{" +
                "player=" + player +
                ", selector='" + selector + '\'' +
                ", instruction=" + instruction +
                '}';
    }
}
