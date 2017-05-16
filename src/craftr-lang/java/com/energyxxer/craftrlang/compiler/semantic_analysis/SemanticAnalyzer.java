package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.compiler.Compiler;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.PackageManager;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.TypeRegistry;
import com.energyxxer.util.out.Console;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 3/3/2017.
 */
public class SemanticAnalyzer {
    public final Compiler compiler;

    public final File rootPath;
    public final TypeRegistry typeRegistry;
    public final ArrayList<CraftrFile> files;

    private final SymbolTable symbolTable;

    private final PackageManager packageManager;

    public SemanticAnalyzer(Compiler compiler, HashMap<File, TokenPattern<?>> filePatterns, File rootPath) {
        this.compiler = compiler;
        this.rootPath = rootPath;
        this.typeRegistry = new TypeRegistry();
        this.files = new ArrayList<>();
        this.symbolTable = new SymbolTable(compiler);
        this.symbolTable.setRootSkipName("src");
        this.packageManager = new PackageManager(this.symbolTable);

        try {
            for(File f : filePatterns.keySet()) {
                files.add(new CraftrFile(this, f, filePatterns.get(f)));
            }

            for(CraftrFile f : files) {
                f.initImports();
            }

        } catch(CraftrException e) {
            Console.err.println(e.getMessage());
            return;
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public PackageManager getPackageManager() {
        return packageManager;
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
