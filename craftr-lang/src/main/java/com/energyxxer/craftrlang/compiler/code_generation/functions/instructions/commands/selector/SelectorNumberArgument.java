package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.selector;

public class SelectorNumberArgument {
    private Integer min;
    private Integer max;

    public SelectorNumberArgument(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public SelectorNumberArgument(int value) {
        this.min = value;
        this.max = value;
    }

    @Override
    public String toString() {
        if(min != null && max != null && min.equals(max)) {
            return String.valueOf(min);
        } else {
            return ((min != null) ? ""+min : "") + ".." + ((max != null) ? ""+max : "");
        }
    }
}
