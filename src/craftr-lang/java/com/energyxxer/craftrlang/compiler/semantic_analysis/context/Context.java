package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;

/**
 * Created by User on 5/16/2017.
 */
public class Context {
    private CraftrFile file = null;
    private Unit unit = null;

    public Context(Unit unit) {
        this.file = unit.getDeclaringFile();
        this.unit = unit;
    }

    public Context(CraftrFile file) {
        this.file = file;
    }

    public CraftrFile getFile() {
        return file;
    }

    public Unit getUnit() {
        return unit;
    }
}
