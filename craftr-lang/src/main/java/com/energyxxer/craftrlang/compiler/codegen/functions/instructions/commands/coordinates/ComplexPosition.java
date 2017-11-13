package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ComplexPosition implements CoordinateModifier {
    private ArrayList<CoordinateModifier> modifiers = new ArrayList<>();

    public ComplexPosition(CoordinateModifier... modifiers) {
        this(Arrays.asList(modifiers));
    }

    public ComplexPosition(Collection<CoordinateModifier> modifiers) {
        this.modifiers.addAll(modifiers);
        purge();
    }

    private void purge() {
        for(int i = 0; i < modifiers.size(); i++) {
            CoordinateModifier modifier = modifiers.get(i);
            if(modifier.isAbsolute()) {
                for(int j = 0; j < i; j++) {
                    modifiers.remove(0);
                }
                i = -1;
            } else if(!modifier.isSignificant()) {
                modifiers.remove(i);
                i--;
            }
        }
    }

    public void addModifier(CoordinateModifier modifier) {
        this.modifiers.add(modifier);
        purge();
    }

    @Override
    public String getSubCommand() {
        StringBuilder sb = new StringBuilder();
        modifiers.forEach(m -> {
            sb.append(m.getSubCommand());
            sb.append(' ');
        });
        sb.setLength(Math.max(sb.length()-1,0));
        return sb.toString();
    }

    @Override
    public boolean isIdempotent() {
        for(CoordinateModifier modifier : modifiers) {
            if(!modifier.isIdempotent()) return false;
        }
        return true;
    }

    @Override
    public boolean isSignificant() {
        for(CoordinateModifier modifier : modifiers) {
            if(modifier.isSignificant()) return true;
        }
        return false;
    }

    @Override
    public boolean isAbsolute() {
        for(CoordinateModifier modifier : modifiers) {
            if(modifier.isAbsolute()) return true;
        }
        return false;
    }
}
