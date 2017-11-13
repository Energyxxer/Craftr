package com.energyxxer.craftrlang.compiler.codegen.functions;

import com.energyxxer.craftrlang.compiler.Compiler;

import java.util.ArrayList;

public class MCFunctionManager {
    private final Compiler compiler;
    private ArrayList<MCFunction> allFunctions = new ArrayList<>();

    public MCFunctionManager(Compiler compiler) {
        this.compiler = compiler;
    }

    public MCFunction createFunction(String name) {
        MCFunction newFunction = new MCFunction(name);
        this.allFunctions.add(newFunction);
        return newFunction;
    }

    //TODO Create a function builder here, and package-privatize the MCFunction constructor.
}
