package com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package;

import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;

/**
 * Created by User on 4/9/2017.
 */
public class PackageManager {
    private Package root;

    public PackageManager(SymbolTable rootTable) {
        this.root = new Package(rootTable);
    }

    public Package createPackage(String path) {
        return root.createPackage(path);
    }
}
