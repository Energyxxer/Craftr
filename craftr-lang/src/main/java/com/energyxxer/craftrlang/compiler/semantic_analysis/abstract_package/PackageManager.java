package com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package;

import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;

/**
 * Created by User on 4/9/2017.
 */
public class PackageManager {
    private final SemanticAnalyzer analyzer;
    private Package root;

    public PackageManager(SymbolTable rootTable, SemanticAnalyzer analyzer) {
        this.root = new Package(rootTable);
        this.analyzer = analyzer;
    }

    public Package createPackage(String path) {
        Package np = root.createPackage(path);
        if(path.equals("craftr.lang")) analyzer.setLangPackage(np);
        return np;
    }

    public Package getRoot() {
        return root;
    }
}
